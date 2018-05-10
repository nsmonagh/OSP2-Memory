package osp.Memory;

import java.lang.Math;

import osp.IFLModules.IflPageTable;
import osp.Tasks.TaskCB;

public class PageTable extends IflPageTable {
	PageTableEntry[] pages;
	
	public PageTable(TaskCB ownerTask) {
		super(ownerTask);
		pages = new PageTableEntry[(int) Math.pow(2, MMU.getPageAddressBits())];
		for (int i = 0; i < pages.length; i++) {
			pages[i] = new PageTableEntry(this, i);
		}
	}

	public void do_deallocateMemory() {
		TaskCB task = getTask();
		for (FrameTableEntry frame : MMU.frameTable) {
			//PageTableEntry page = frame.getPage();
			frame.setPage(null);
			frame.setDirty(false);
			frame.setReferenced(false);
			if (task == frame.getReserved())
				frame.setUnreserved(task);
		}
	}
}
