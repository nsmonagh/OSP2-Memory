package osp.Memory;

import java.util.ArrayList;

import osp.Hardware.CPU;
import osp.IFLModules.IflMMU;
import osp.Interrupts.InterruptVector;
import osp.Threads.ThreadCB;

public class MMU extends IflMMU {
	static ArrayList<FrameTableEntry> frameTable;
	
	public static void init() {
		for (int i = 0; i < MMU.getFrameTableSize(); i++) {
			setFrame(i, new FrameTableEntry(i));
		}
		frameTable = new ArrayList<FrameTableEntry>();
	}

	static public PageTableEntry do_refer(int memoryAddress, int referenceType, ThreadCB thread) {
		int pageAddress = (int) (memoryAddress/Math.pow(2, getVirtualAddressBits()-getPageAddressBits()));
		PageTableEntry page = getPTBR().pages[pageAddress];
		FrameTableEntry pageFrame = page.getFrame();
		
		if (page.isValid()) {
			frameTable.add(pageFrame);
			pageFrame.setReferenced(true);
			if (referenceType == MemoryWrite)
				pageFrame.setDirty(true);
			return page;
		}
		else
			if (page.getValidatingThread() != null)
				thread.suspend(page);
			else {
				InterruptVector.setPage(page);
				InterruptVector.setReferenceType(referenceType);
				InterruptVector.setThread(thread);
				CPU.interrupt(PageFault);
			}
		if (thread.getStatus() != ThreadKill) {
			pageFrame.setReferenced(true);
			if (referenceType == MemoryWrite)
				pageFrame.setDirty(true);
		}
		return page;
	}

	public static void atError() {}

	public static void atWarning() {}
}
