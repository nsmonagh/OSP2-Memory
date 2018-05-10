package osp.Memory;

import osp.Devices.IORB;
import osp.IFLModules.IflPageTableEntry;
import osp.Threads.ThreadCB;

public class PageTableEntry extends IflPageTableEntry {
	public PageTableEntry(PageTable ownerPageTable, int pageNumber) {
		super(ownerPageTable, pageNumber);
	}

	public int do_lock(IORB iorb) {
		ThreadCB thread = iorb.getThread();
		
		if (!isValid()) {
			if (getValidatingThread() == null) {
				PageFaultHandler.handlePageFault(thread, MemoryLock, this);
			} 
			else {
				if (getValidatingThread()!= thread) {
					thread.suspend(this);
					if (thread.getStatus() == ThreadKill)
						return FAILURE;
				}
			}
		}
		
		getFrame().incrementLockCount();
		return SUCCESS;
	}

	public void do_unlock() {
		FrameTableEntry frame = getFrame();
		
		if (frame.getLockCount() > 0)
			frame.decrementLockCount();
	}
}
