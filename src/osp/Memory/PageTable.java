package osp.Memory;

import java.lang.Math;
import osp.Tasks.*;
import osp.Utilities.*;
import osp.IFLModules.*;
import osp.Hardware.*;

/**
	The PageTable class represents the page table for a given task.
	A PageTable consists of an array of PageTableEntry objects.  This
	page table is of the non-inverted type.

	@OSPProject Memory
*/

public class PageTable extends IflPageTable {
	PageTableEntry[] pageTable;
	
	public PageTable(TaskCB ownerTask) {
		super(ownerTask);
		pageTable = new PageTableEntry[(int) Math.pow(2, MMU.getPageAddressBits())];
	}

	/**
	   Frees up main memory occupied by the task.
	   Then unreserves the freed pages, if necessary.

	   @OSPProject Memory
	*/
	public void do_deallocateMemory() {
		
	}
}
