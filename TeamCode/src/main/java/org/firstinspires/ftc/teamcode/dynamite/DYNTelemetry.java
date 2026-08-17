package org.firstinspires.ftc.teamcode.dynamite;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.jetbrains.annotations.ApiStatus.Experimental;

import java.util.function.Consumer;

/**
 * Telemetry implementation that forwards data into DYN's own buffer instead of
 * the classic OpMode Telemetry object.
 *
 * Wiring assumptions (adjust pushRow()/update() if your buffer expects something else):
 *  - adder is invoked once per addData()/setValue()/log call with a single-row
 *    2D array: {{caption, value}}. It's a Consumer<String[][]> rather than
 *    Consumer<String[]> so a single call site can also accept multi-row pushes
 *    if you ever batch them.
 *  - updater is your DYN-side flush/render call (equivalent to classic
 *    telemetry.update()) — this is where you'd clear telemBuffer if autoClear
 *    is true, so the stale-entry bug doesn't reappear here.
 */
@Experimental
public class DYNTelemetry implements Telemetry {

    private final Consumer<String[]> adder;
    private final Runnable updater;

    private boolean autoClear = true;
    private int msTransmissionInterval = 250;
    private String itemSeparator = " | ";
    private String captionValueSeparator = " : ";
    private DisplayFormat displayFormat = DisplayFormat.CLASSIC;
    private final DYNLog log = new DYNLog();

    public DYNTelemetry(Consumer<String[]> addData, Runnable update) {
        adder = addData;
        updater = update;
    }

    // ---------------- addData variants ----------------

    @Override
    public Item addData(String caption, String format, Object... args) {
        String value = String.format(format, args);
        pushRow(caption, value);
        return new DYNItem(caption, value);
    }

    @Override
    public Item addData(String caption, Object value) {
        String v = String.valueOf(value);
        pushRow(caption, v);
        return new DYNItem(caption, v);
    }

    @Override
    public <T> Item addData(String caption, Func<T> valueProducer) {
        String v = String.valueOf(valueProducer.value());
        pushRow(caption, v);
        return new DYNItem(caption, v);
    }

    @Override
    public <T> Item addData(String caption, String format, Func<T> valueProducer) {
        String v = String.format(format, valueProducer.value());
        pushRow(caption, v);
        return new DYNItem(caption, v);
    }

    private void pushRow(String caption, String value) {
        adder.accept(new String[]{caption, value});
    }

    // ---------------- item / line lifecycle ----------------

    @Override
    public boolean removeItem(Item item) {
        // DYN buffer owns state on its side; nothing to track here.
        return false;
    }

    @Override
    public void clear() {
        // Intentionally a no-op — this wrapper is stateless. If you want
        // clear()/autoClear to actually drop rows, call into a clear hook on
        // the DYN buffer here (mirrors update() below).
    }

    @Override
    public void clearAll() {
        clear();
    }

    @Override
    public Object addAction(Runnable action) {
        action.run();
        return action;
    }

    @Override
    public boolean removeAction(Object token) {
        return false;
    }

    @Override
    public boolean update() {
        updater.run();
        return true;
    }

    @Override
    public Line addLine() {
        return new DYNLine();
    }

    @Override
    public Line addLine(String lineCaption) {
        pushRow(lineCaption, "");
        return new DYNLine();
    }

    @Override
    public boolean removeLine(Line line) {
        return false;
    }

    // ---------------- config knobs ----------------

    @Override
    public boolean isAutoClear() {
        return autoClear;
    }

    @Override
    public void setAutoClear(boolean autoClear) {
        this.autoClear = autoClear;
    }

    @Override
    public int getMsTransmissionInterval() {
        return msTransmissionInterval;
    }

    @Override
    public void setMsTransmissionInterval(int msTransmissionInterval) {
        this.msTransmissionInterval = msTransmissionInterval;
    }

    @Override
    public String getItemSeparator() {
        return itemSeparator;
    }

    @Override
    public void setItemSeparator(String itemSeparator) {
        this.itemSeparator = itemSeparator;
    }

    @Override
    public String getCaptionValueSeparator() {
        return captionValueSeparator;
    }

    @Override
    public void setCaptionValueSeparator(String captionValueSeparator) {
        this.captionValueSeparator = captionValueSeparator;
    }

    @Override
    public void setDisplayFormat(DisplayFormat displayFormat) {
        this.displayFormat = displayFormat;
    }

    @Override
    public void speak(String text) {
        // No audio channel wired up on the DYN side yet — no-op.
    }

    @Override
    public void speak(String text, String languageCode, String countryCode) {
        speak(text);
    }

    @Override
    public Log log() {
        return log;
    }

    // ---------------- Item ----------------

    private class DYNItem implements Item {
        private String caption;
        private String value;
        private Boolean retained = false;

        DYNItem(String caption, String value) {
            this.caption = caption;
            this.value = value;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public Item setCaption(String caption) {
            this.caption = caption;
            return this;
        }

        @Override
        public Item setValue(String format, Object... args) {
            value = String.format(format, args);
            pushRow(caption, value);
            return this;
        }

        @Override
        public Item setValue(Object value) {
            this.value = String.valueOf(value);
            pushRow(caption, this.value);
            return this;
        }

        @Override
        public <T> Item setValue(Func<T> valueProducer) {
            value = String.valueOf(valueProducer.value());
            pushRow(caption, value);
            return this;
        }

        @Override
        public <T> Item setValue(String format, Func<T> valueProducer) {
            value = String.format(format, valueProducer.value());
            pushRow(caption, value);
            return this;
        }

        @Override
        public Item setRetained(Boolean retained) {
            this.retained = retained;
            return this;
        }

        @Override
        public boolean isRetained() {
            return retained;
        }

        @Override
        public Item addData(String caption, String format, Object... args) {
            return DYNTelemetry.this.addData(caption, format, args);
        }

        @Override
        public Item addData(String caption, Object value) {
            return DYNTelemetry.this.addData(caption, value);
        }

        @Override
        public <T> Item addData(String caption, Func<T> valueProducer) {
            return DYNTelemetry.this.addData(caption, valueProducer);
        }

        @Override
        public <T> Item addData(String caption, String format, Func<T> valueProducer) {
            return DYNTelemetry.this.addData(caption, format, valueProducer);
        }
    }

    // ---------------- Line ----------------

    private class DYNLine implements Line {
        @Override
        public Item addData(String caption, String format, Object... args) {
            return DYNTelemetry.this.addData(caption, format, args);
        }

        @Override
        public Item addData(String caption, Object value) {
            return DYNTelemetry.this.addData(caption, value);
        }

        @Override
        public <T> Item addData(String caption, Func<T> valueProducer) {
            return DYNTelemetry.this.addData(caption, valueProducer);
        }

        @Override
        public <T> Item addData(String caption, String format, Func<T> valueProducer) {
            return DYNTelemetry.this.addData(caption, format, valueProducer);
        }
    }

    // ---------------- Log ----------------

    private class DYNLog implements Log {
        private DisplayOrder order = DisplayOrder.OLDEST_FIRST;
        private int capacity = 8;

        @Override
        public int getCapacity() {
            return capacity;
        }

        @Override
        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public DisplayOrder getDisplayOrder() {
            return order;
        }

        @Override
        public void setDisplayOrder(DisplayOrder displayOrder) {
            order = displayOrder;
        }

        @Override
        public void add(String entry) {

        }

        @Override
        public void add(String lineFmt, Object... args) {
            pushRow("log", String.format(lineFmt, args));
        }

        @Override
        public void clear() {

        }
    }
}