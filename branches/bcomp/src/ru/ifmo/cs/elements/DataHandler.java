/**
 * $Id$
 */

package ru.ifmo.cs.elements;

import java.util.ArrayList;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataHandler extends DataStorage {
	private ArrayList<DataDestination> dests = new ArrayList<DataDestination>();

	public DataHandler(int width, DataSource ... inputs) {
		super(width, inputs);
	}

	public void addDestination(DataDestination dest) {
		dests.add(dest);
	}

	public void removeDestination(DataDestination dest) {
		int index = dests.indexOf(dest);

		if (index >= 0)
			dests.remove(index);
	}

	public void setValue(int value) {
		super.setValue(value);

		for (DataDestination dest : dests)
			dest.setValue(this.value);
	}

	public void resetValue() {
		super.setValue(0);
	}
}
