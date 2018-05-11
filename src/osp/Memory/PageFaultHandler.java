package osp.Memory;

import osp.FileSys.OpenFile;
import osp.IFLModules.IflPageFaultHandler;
import osp.IFLModules.SystemEvent;
import osp.Threads.ThreadCB;

public class PageFaultHandler extends IflPageFaultHandler {
	public static int do_handlePageFault(ThreadCB thread, int referenceType, PageTableEntry page) {
		if (page.isValid()) {
			page.notifyThreads();
			ThreadCB.dispatch();
			return FAILURE;
		}
		
		SystemEvent pfEvent = new SystemEvent("PageFault");
		thread.suspend(pfEvent);
		
		boolean flag = true;
		FrameTableEntry pfFrame = null;
		
		for (FrameTableEntry frame : MMU.frameTable) {
			boolean locked = frame.getLockCount() > 0;
			boolean reserved = frame.isReserved();
			if (!locked || !reserved) {
				pfFrame = frame;
				pfFrame.setReserved(thread.getTask());
				flag = false;
				break;
			}
		}
		
		if (MMU.frameTable.size() != 0 && flag) {
			page.notifyThreads();
			pfEvent.notifyThreads();
			ThreadCB.dispatch();
			return NotEnoughMemory;
		}
		
		page.setValidatingThread(thread);
		
		try {
			PageTableEntry pfFramePage = pfFrame.getPage();
			
			if (pfFramePage != null && pfFrame.isDirty()) {
				OpenFile swapFile = pfFramePage.getTask().getSwapFile();
				if (thread.getStatus() != ThreadKill) {
					swapFile.write(pfFramePage.getID(), pfFramePage, thread);
					pfFrame.setDirty(false);
				}
				else {
					page.notifyThreads();
					pfEvent.notifyThreads();
					ThreadCB.dispatch();
					return FAILURE;
				}
			}
			else if (pfFramePage != null) {
				pfFrame.setReferenced(false);
				pfFramePage.setValid(false);
				pfFramePage.setFrame(null);
				if (pfFramePage.getTask().getStatus() != TaskTerm)
					pfFrame.setPage(null);
			}
		} catch (NullPointerException e) {}
		
		try {
			if (thread.getStatus() == ThreadKill) {
				page.notifyThreads();
				page.setValidatingThread(null);
				page.setFrame(null);
				pfEvent.notifyThreads();
				ThreadCB.dispatch();
				return FAILURE;
			}
			else {
				page.setFrame(pfFrame);
				OpenFile swapFile = page.getTask().getSwapFile();
				swapFile.read(page.getID(), page, thread);
				pfFrame.setDirty(false);
			}
		} catch (NullPointerException e) {}
		
		try {
			boolean threadKill = thread.getStatus() == ThreadKill;
			boolean pageExists = pfFrame.getPage() != null;
			boolean sameTask = pfFrame.getPage().getTask() == thread.getTask();
				
			if (threadKill && pageExists && sameTask) {
				pfFrame.setPage(null);
				page.notifyThreads();
				page.setValidatingThread(null);
				pfFrame.getPage().setFrame(null);
				pfEvent.notifyThreads();
				ThreadCB.dispatch();
				return FAILURE;
			}
			else {
				pfFrame.setPage(page);
				page.setValid(true);
			}
		} catch (NullPointerException e) {}
		
		if (thread.getStatus() == ThreadKill) {
			if(thread.getTask().getStatus() == TaskTerm) {
				pfFrame.setPage(null);
				pfFrame.setReferenced(false);
			}
			page.setValid(false);
			page.setFrame(null);
			page.notifyThreads();
			pfEvent.notifyThreads();
			ThreadCB.dispatch();
			return FAILURE;
		}
		
		page.setValidatingThread(null);
		page.notifyThreads();
		pfEvent.notifyThreads();
		ThreadCB.dispatch();
		return SUCCESS;
	}
}
