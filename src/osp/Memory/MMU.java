package osp.Memory;

import java.util.*;
import osp.IFLModules.*;
import osp.Threads.*;
import osp.Tasks.*;
import osp.Utilities.*;
import osp.Hardware.*;
import osp.Interrupts.*;

public class MMU extends IflMMU {
	static FrameTableEntry[] frameTable;
	
	public static void init() {
		for () {
			setFrame(i, new FrameTableEntry(i));
		}
		setFrame(bh, null);
		frameTable = new FrameTableEntry[MMU.getFrameTableSize()];
	}

	/**
	   This method handles memory references. The method must 
	   calculate, which memory page contains the memoryAddress,
	   determine, whether the page is valid, start page fault 
	   by making an interrupt if the page is invalid, finally, 
	   if the page is still valid, i.e., not swapped out by another 
	   thread while this thread was suspended, set its frame
	   as referenced and then set it as dirty if necessary.
	   (After pagefault, the thread will be placed on the ready queue, 
	   and it is possible that some other thread will take away the frame.)
	   
	   @param memoryAddress A virtual memory address
	   @param referenceType The type of memory reference to perform 
	   @param thread that does the memory access
	   (e.g., MemoryRead or MemoryWrite).
	   @return The referenced page.

	   @OSPProject Memory
	*/
	static public PageTableEntry do_refer(int memoryAddress, int referenceType, ThreadCB thread) {
		return null;
	}

	public static void atError() {}

	public static void atWarning() {}
}
