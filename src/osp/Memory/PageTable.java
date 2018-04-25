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
	public PageTable(TaskCB ownerTask) {
		super(ownerTask);
		//more
	}

	/**
	   Frees up main memory occupied by the task.
	   Then unreserves the freed pages, if necessary.

	   @OSPProject Memory
	*/
	public void do_deallocateMemory() {
		
	}
}
