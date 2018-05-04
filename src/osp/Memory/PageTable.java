package osp.Memory;

import java.lang.Math;
import osp.Tasks.*;
import osp.Utilities.*;
import osp.IFLModules.*;
import osp.Hardware.*;

public class PageTable extends IflPageTable {
	PageTableEntry[] pages;
	
	public PageTable(TaskCB ownerTask) {
		super(ownerTask);
		pages = new PageTableEntry[(int) Math.pow(2, MMU.getPageAddressBits())];
	}

	/**
	   Frees up main memory occupied by the task.
	   Then unreserves the freed pages, if necessary.

	   @OSPProject Memory
	*/
	public void do_deallocateMemory() {
		PageTableEntry.getFrame();
		PageTableEntry[] taskPageTable = getTask().getPageTable().pages;
		for (PageTableEntry page : taskPageTable) {
			setPage()
		}
	}
}
