package osp.Memory;

import osp.Hardware.*;
import osp.Tasks.*;
import osp.Threads.*;
import osp.Devices.*;
import osp.Utilities.*;
import osp.IFLModules.*;

/**
   The PageTableEntry object contains information about a specific virtual
   page in memory, including the page frame in which it resides.
   
   @OSPProject Memory

*/

public class PageTableEntry extends IflPageTableEntry {
	public PageTableEntry(PageTable ownerPageTable, int pageNumber) {
		super(ownerPageTable, pageNumber);
	}

	/**
	   This method increases the lock count on the page by one. 

	The method must FIRST increment lockCount, THEN  
	check if the page is valid, and if it is not and no 
	page validation event is present for the page, start page fault 
	by calling PageFaultHandler.handlePageFault().

	@return SUCCESS or FAILURE
	FAILURE happens when the pagefault due to locking fails or the 
	that created the IORB thread gets killed.

	@OSPProject Memory
	 */
	public int do_lock(IORB iorb) {
		
		ThreadCB thread = iorb.getThread();
		
		
		if (!isValid()) {
			if(getValidatingThread() == null) {
				PageFaultHandler.handlePageFault(thread, MemoryLock, this);
			} 
			else {
				if(getValidatingThread()!= thread) {
					thread.suspend(this);
					if(thread.getStatus() == ThreadKill)
						return FAILURE;
				}
			}
		}
		
		
		getFrame().incrementLockCount();
		
		return SUCCESS;
	}

	/** This method decreases the lock count on the page by one. 

	This method must decrement lockCount, but not below zero.

	@OSPProject Memory
	*/
	public void do_unlock() {
		
		if (getFrame().getLockCount() > 0)
			getFrame().decrementLockCount();
		else
			System.err.println("The locks value went negative. This indicates an error.");
	}
}
