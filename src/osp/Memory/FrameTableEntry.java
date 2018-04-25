package osp.Memory;

import osp.Tasks.*;
import osp.Interrupts.*;
import osp.Utilities.*;
import osp.IFLModules.IflFrameTableEntry;

/**
	The FrameTableEntry class contains information about a specific page
	frame of memory.

	@OSPProject Memory
*/

public class FrameTableEntry extends IflFrameTableEntry {
	public FrameTableEntry(int frameID) {
		super(frameID);
		//more
	}
}
