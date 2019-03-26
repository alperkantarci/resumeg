package com.etp.resumeg.resumeg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.filter.IEventFilter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;

public class CustomFilteredEventListener implements IEventListener {

	protected final List<IEventListener> delegates;
	protected final List<IEventFilter[]> filters;
	
	

	/**
	 * Constructs a {@link FilteredEventListener} empty instance. Use
	 * {@link #attachEventListener(IEventListener, IEventFilter...)} to add an event
	 * listener along with its filters.
	 */
	public CustomFilteredEventListener() {
		this.delegates = new ArrayList<>();
		this.filters = new ArrayList<>();
	}

	/**
	 * Constructs a {@link FilteredEventListener} instance with one delegate. Use
	 * {@link #attachEventListener(IEventListener, IEventFilter...)} to add more
	 * {@link IEventListener} delegates along with their filters.
	 * 
	 * @param delegate  a delegate that fill be called when all the corresponding
	 *                  filters for an event pass
	 * @param filterSet filters attached to the delegate that will be tested before
	 *                  passing an event on to the delegate
	 */
	public CustomFilteredEventListener(IEventListener delegate, IEventFilter... filterSet) {
		this();
		attachEventListener(delegate, filterSet);
	}

	/**
	 * Attaches another {@link IEventListener} delegate with its filters. When all
	 * the filters attached to the delegate for an event accept the event, the event
	 * will be passed on to the delegate. You can attach multiple delegates to this
	 * {@link FilteredEventListener} instance. The content stream will be parsed
	 * just once, so it is better for performance than creating multiple
	 * {@link FilteredEventListener} instances and parsing the content stream
	 * multiple times. This is useful, for instance, when you want to extract
	 * content from multiple regions of a page.
	 * 
	 * @param delegate  a delegate that fill be called when all the corresponding
	 *                  filters for an event pass
	 * @param filterSet filters attached to the delegate that will be tested before
	 *                  passing an event on to the delegate
	 * @return delegate that has been passed to the method, used for convenient call
	 *         chaining
	 */
	public <T extends IEventListener> T attachEventListener(T delegate, IEventFilter... filterSet) {
		delegates.add(delegate);
		filters.add(filterSet);

		return delegate;
	}

	public void eventOccurred(IEventData data, EventType type) {
		for (int i = 0; i < delegates.size(); i++) {
			IEventListener delegate = delegates.get(i);
			boolean filtersPassed = delegate.getSupportedEvents() == null
					|| delegate.getSupportedEvents().contains(type);
			for (IEventFilter filter : filters.get(i)) {
				if (!filter.accept(data, type)) {
					filtersPassed = false;
					break;
				}
			}
			if (filtersPassed) {
				delegate.eventOccurred(data, type);
			}
		}
	}

	public Set<EventType> getSupportedEvents() {
		return null;
	}

}
