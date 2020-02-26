package com.luke.dicom;


import org.eclipse.swt.*;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.printing.*;
import org.eclipse.swt.widgets.*;
import org.junit.Test;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void test_01(){
        final Display display = new Display ();
        final Image image = new Image (display, 16, 16);
        GC gc = new GC (image);
        gc.setBackground (display.getSystemColor (SWT.COLOR_RED));
        gc.fillRectangle (image.getBounds ());
        gc.dispose ();
        final Shell shell = new Shell (display);
        shell.setText ("Lazy Table");
        shell.setLayout (new FillLayout());
        final Table table = new Table (shell, SWT.BORDER | SWT.MULTI);
        table.setSize (200, 200);
        //创建一个线程
        Thread thread = new Thread () {
            public void run () {
                for (int i=0; i<20000; i++) {
                    if (table.isDisposed ()) return;
                    final int [] index = new int [] {i};
                    //必须使用display的 syncExec 或者 asyncExec 方法来调用
                    display.syncExec (new Runnable () {
                        public void run () {
                            if (table.isDisposed ()) return;
                            TableItem item = new TableItem (table, SWT.NONE);
                            item.setText ("Table Item " + index [0]);
                            item.setImage (image);
                        }
                    });
                    // 如果使用下面的注释方法则会抛出：org.eclipse.swt.SWTException: Invalid thread access
//				if (table.isDisposed ()) return;
//				TableItem item = new TableItem (table, SWT.NONE);
//				item.setText ("Table Item " + index [0]);
//				item.setImage (image);
                }
            }
        };
        thread.start ();
        shell.setSize (200, 200);
        shell.open ();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        image.dispose ();
        display.dispose ();
    }

    @Test
    public void test_120(){
        Display display = new Display ();
        Shell shell = new Shell (display);
        shell.setText("Snippet 120");
        shell.setSize (200, 200);
        Monitor primary = display.getPrimaryMonitor ();
        Rectangle bounds = primary.getBounds ();
        Rectangle rect = shell.getBounds ();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        shell.setLocation (x, y);
        shell.open ();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }

    /**
     * bring up a browser
     */
    @Test
    public void test_128_browser(){
            Display display = new Display();
            final Shell shell = new Shell(display);
            shell.setText("Snippet 128");
            GridLayout gridLayout = new GridLayout();
            gridLayout.numColumns = 3;
            shell.setLayout(gridLayout);
            ToolBar toolbar = new ToolBar(shell, SWT.NONE);
            ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
            itemBack.setText("Back");
            ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
            itemForward.setText("Forward");
            ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
            itemStop.setText("Stop");
            ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
            itemRefresh.setText("Refresh");
            ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
            itemGo.setText("Go");

            GridData data = new GridData();
            data.horizontalSpan = 3;
            toolbar.setLayoutData(data);

            Label labelAddress = new Label(shell, SWT.NONE);
            labelAddress.setText("Address");

            final Text location = new Text(shell, SWT.BORDER);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.horizontalSpan = 2;
            data.grabExcessHorizontalSpace = true;
            location.setLayoutData(data);

            final Browser browser;
            try {
                browser = new Browser(shell, SWT.NONE);
            } catch (SWTError e) {
                System.out.println("Could not instantiate Browser: " + e.getMessage());
                display.dispose();
                return;
            }
            data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.verticalAlignment = GridData.FILL;
            data.horizontalSpan = 3;
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            browser.setLayoutData(data);

            final Label status = new Label(shell, SWT.NONE);
            data = new GridData(GridData.FILL_HORIZONTAL);
            data.horizontalSpan = 2;
            status.setLayoutData(data);

            final ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
            data = new GridData();
            data.horizontalAlignment = GridData.END;
            progressBar.setLayoutData(data);

            /* event handling */
            Listener listener = event -> {
                ToolItem item = (ToolItem) event.widget;
                String string = item.getText();
                if (string.equals("Back"))
                    browser.back();
                else if (string.equals("Forward"))
                    browser.forward();
                else if (string.equals("Stop"))
                    browser.stop();
                else if (string.equals("Refresh"))
                    browser.refresh();
                else if (string.equals("Go"))
                    browser.setUrl(location.getText());
            };
            browser.addProgressListener(new ProgressListener() {
                @Override
                public void changed(ProgressEvent event) {
                    if (event.total == 0) return;
                    int ratio = event.current * 100 / event.total;
                    progressBar.setSelection(ratio);
                }
                @Override
                public void completed(ProgressEvent event) {
                    progressBar.setSelection(0);
                }
            });
            browser.addStatusTextListener(event -> status.setText(event.text));
            browser.addLocationListener(LocationListener.changedAdapter(event -> {
                        if (event.top) location.setText(event.location);
                    }
            ));
            itemBack.addListener(SWT.Selection, listener);
            itemForward.addListener(SWT.Selection, listener);
            itemStop.addListener(SWT.Selection, listener);
            itemRefresh.addListener(SWT.Selection, listener);
            itemGo.addListener(SWT.Selection, listener);
            location.addListener(SWT.DefaultSelection, e -> browser.setUrl(location.getText()));

            shell.open();
            browser.setUrl("http://eclipse.org");

            while (!shell.isDisposed()) {
                if (!display.readAndDispatch())
                    display.sleep();
            }
            display.dispose();
    }

    /**
     * print
     */
    @Test
    public void test_132_print(){
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Snippet 132");
        shell.open ();
        PrinterData data = Printer.getDefaultPrinterData();
        if (data == null) {
            System.out.println("Warning: No default printer.");
            display.dispose();
            return;
        }
        Printer printer = new Printer(data);
        if (printer.startJob("SWT Printing Snippet")) {
            Color black = printer.getSystemColor(SWT.COLOR_BLACK);
            Color white = printer.getSystemColor(SWT.COLOR_WHITE);
            Color red = printer.getSystemColor(SWT.COLOR_RED);
            Rectangle trim = printer.computeTrim(0, 0, 0, 0);
            Point dpi = printer.getDPI();
            int leftMargin = dpi.x + trim.x; // one inch from left side of paper
            if (leftMargin < 0) leftMargin = -trim.x;  // make sure to print on the printable area
            int topMargin = dpi.y / 2 + trim.y; // one-half inch from top edge of paper
            if (topMargin < 0) topMargin = -trim.y;  // make sure to print on the printable area
            GC gc = new GC(printer);
            if (printer.startPage()) {
                gc.setBackground(white);
                gc.setForeground(black);
                String testString = "Hello World!";
                Point extent = gc.stringExtent(testString);
                gc.drawString(testString, leftMargin, topMargin);
                gc.setForeground(red);
                gc.drawRectangle(leftMargin, topMargin, extent.x, extent.y);
                printer.endPage();
            }
            gc.dispose();
            printer.endJob();
        }
        printer.dispose();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose();
    }

    @Test
    public void test_136_readHtml(){
        String html = "<HTML><HEAD><TITLE>HTML Test</TITLE></HEAD><BODY>";
        for (int i = 0; i < 100; i++) html += "<P>This is line "+i+"</P>";
        html += "</BODY></HTML>";

        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Snippet 136");
        shell.setLayout(new FillLayout());
        Browser browser;
        try {
            browser = new Browser(shell, SWT.NONE);
        } catch (SWTError e) {
            System.out.println("Could not instantiate Browser: " + e.getMessage());
            display.dispose();
            return;
        }
        browser.setText(html);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    @Test
    public void test_130_cursor(){
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setText("Snippet 130");
        shell.setLayout(new GridLayout());
        final Text text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        text.setLayoutData(new GridData(GridData.FILL_BOTH));
        final int[] nextId = new int[1];
        Button b = new Button(shell, SWT.PUSH);
        b.setText("invoke long running job");
        b.addSelectionListener(widgetSelectedAdapter(e-> {
            Runnable longJob = new Runnable() {
                boolean done = false;
                int id;
                @Override
                public void run() {
                    Thread thread = new Thread(() -> {
                        id = nextId[0]++;
                        display.syncExec(() -> {
                            if (text.isDisposed()) return;
                            text.append("\nStart long running task "+id);
                        });
                        for (int i = 0; i < 100000; i++) {
                            if (display.isDisposed()) return;
                            System.out.println("do task that takes a long time in a separate thread "+id);
                        }
                        if (display.isDisposed()) return;
                        display.syncExec(() -> {
                            if (text.isDisposed()) return;
                            text.append("\nCompleted long running task "+id);
                        });
                        done = true;
                        display.wake();
                    });
                    thread.start();
                    while (!done && !shell.isDisposed()) {
                        if (!display.readAndDispatch())
                            display.sleep();
                    }
                }
            };
            BusyIndicator.showWhile(display, longJob);
        }));
        shell.setSize(250, 150);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    /**
     * 显示时间弹出窗
     */
    @Test
    public void test_251_datatimeDialog(){
        Display display = new Display ();
        final Shell shell = new Shell (display);
        shell.setText("Snippet 251");
        shell.setLayout(new FillLayout());

        Button open = new Button (shell, SWT.PUSH);
        open.setText ("Open Dialog");
        open.addSelectionListener (widgetSelectedAdapter(e-> {
                    final Shell dialog = new Shell (shell, SWT.DIALOG_TRIM);
                    dialog.setLayout (new GridLayout (3, false));

                    final DateTime calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
                    final DateTime date = new DateTime (dialog, SWT.DATE | SWT.SHORT);
                    final DateTime time = new DateTime (dialog, SWT.TIME | SWT.SHORT);

                    new Label (dialog, SWT.NONE);
                    new Label (dialog, SWT.NONE);
                    Button ok = new Button (dialog, SWT.PUSH);
                    ok.setText ("OK");
                    ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
                    ok.addSelectionListener (widgetSelectedAdapter(event -> {
                                System.out.println ("Calendar date selected (MM/DD/YYYY) = " + (calendar.getMonth () + 1) + "/" + calendar.getDay () + "/" + calendar.getYear ());
                                System.out.println ("Date selected (MM/YYYY) = " + (date.getMonth () + 1) + "/" + date.getYear ());
                                System.out.println ("Time selected (HH:MM) = " + time.getHours () + ":" + (time.getMinutes () < 10 ? "0" : "") + time.getMinutes ());
                                dialog.close ();
                            }
                    ));
                    dialog.setDefaultButton (ok);
                    dialog.pack ();
                    dialog.open ();
                }
        ));
        shell.pack ();
        shell.open ();

        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }

    /**
     * 计时器
     */
    @Test
    public void test_68_(){
        final Display display = new Display ();
        final Color red = display.getSystemColor (SWT.COLOR_RED);
        final Color blue = display.getSystemColor (SWT.COLOR_BLUE);
        Shell shell = new Shell (display);
        shell.setText("Snippet 68");
        shell.setLayout (new RowLayout());
        Button button = new Button (shell, SWT.PUSH);
        button.setText ("Stop Timer");
        final Label label = new Label (shell, SWT.BORDER);
        label.setBackground (red);
        final int time = 500;
        final Runnable timer = new Runnable () {
            @Override
            public void run () {
                if (label.isDisposed ()) return;
                Color color = label.getBackground ().equals (red) ? blue : red;
                label.setBackground (color);
                display.timerExec (time, this);
            }
        };
        display.timerExec (time, timer);
        button.addListener (SWT.Selection, event -> display.timerExec (-1, timer));
        button.pack ();
        label.setLayoutData (new RowData(button.getSize ()));
        shell.pack ();
        shell.open ();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }


    static boolean createdScreenBar = false;

    static void createMenuBar(Shell s) {
        Menu bar = Display.getCurrent().getMenuBar();
        boolean hasAppMenuBar = (bar != null);
        if (bar == null) {
            bar = new Menu(s, SWT.BAR);
        }

        // Populate the menu bar once if this is a screen menu bar.
        // Otherwise, we need to make a new menu bar for each shell.
        if (!createdScreenBar || !hasAppMenuBar) {
            MenuItem item = new MenuItem(bar, SWT.CASCADE);
            item.setText("File");
            Menu menu = new Menu(item);
            item.setMenu(menu);
            menu.addMenuListener(new MenuListener() {

                @Override
                public void menuHidden(MenuEvent e) {
                    System.out.println("Menu closed: " + e);
                }

                @Override
                public void menuShown(MenuEvent e) {
                    System.out.println("Menu open: " + e);
                }

            });
            MenuItem newWindow = new MenuItem(menu, SWT.PUSH);
            newWindow.setText("New Window");
            newWindow.setAccelerator(SWT.MOD1 | 'N');
            newWindow.addListener(SWT.Selection, event -> {
                Shell s1 = createShell();
                s1.open();
            });
            if (!SWT.getPlatform().equals("cocoa")) {
                MenuItem exit = new MenuItem(menu, SWT.PUSH);
                exit.setText("Exit");
                exit.addListener(SWT.Selection, event -> {
                    Display d = Display.getCurrent();
                    Shell[] shells = d.getShells();
                    for (Shell shell : shells) {
                        shell.close();
                    }
                });
            }
            if (!hasAppMenuBar) s.setMenuBar(bar);
            createdScreenBar = true;
        }
    }

    static Shell createShell() {
        final Shell shell = new Shell(SWT.SHELL_TRIM);
        shell.setText("Snippet 348");
        createMenuBar(shell);

        shell.addDisposeListener(e -> {
            Display d = Display.getCurrent();
            Menu bar = d.getMenuBar();
            boolean hasAppMenuBar = (bar != null);
            if (!hasAppMenuBar) {
                shell.getMenuBar().dispose();
                Shell[] shells = d.getShells();
                if ((shells.length == 1) && (shells[0] == shell)) {
                    if (!d.isDisposed()) d.dispose();
                }
            }
        });

        return shell;
    }


    /**
     * 打开新窗口
     */
    @Test
    public void test_348_(){
        final Display display = new Display();

        Shell shell = createShell();
        shell.open();

        while (!display.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }


    /**
     * 获取窗口上鼠标坐标
     */
    @Test
    public void test_276_(){
        final Display display = new Display ();
        Shell shell = new Shell (display);
        shell.setText("Snippet 276");
        shell.setBounds (200, 200, 400, 400);
        Label label = new Label (shell, SWT.NONE);
        label.setText ("click in shell to print display-relative coordinate");
        Listener listener = event -> {
            Point point = new Point (event.x, event.y);
            System.out.println (display.map ((Control)event.widget, null, point));
        };
        shell.addListener (SWT.MouseDown, listener);
        label.addListener (SWT.MouseDown, listener);
        Rectangle clientArea = shell.getClientArea();
        label.setLocation(clientArea.x, clientArea.y);
        label.pack ();
        shell.open ();
        while (!shell.isDisposed ()){
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }


    static int[] colorIds = new int[] {SWT.COLOR_INFO_BACKGROUND,
            SWT.COLOR_INFO_FOREGROUND,
            SWT.COLOR_LINK_FOREGROUND,
            SWT.COLOR_LIST_BACKGROUND,
            SWT.COLOR_LIST_FOREGROUND,
            SWT.COLOR_LIST_SELECTION,
            SWT.COLOR_LIST_SELECTION_TEXT,
            SWT.COLOR_TEXT_DISABLED_BACKGROUND,
            SWT.COLOR_TITLE_BACKGROUND,
            SWT.COLOR_TITLE_BACKGROUND_GRADIENT,
            SWT.COLOR_TITLE_FOREGROUND,
            SWT.COLOR_TITLE_INACTIVE_BACKGROUND,
            SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT,
            SWT.COLOR_TITLE_INACTIVE_FOREGROUND,
            SWT.COLOR_WIDGET_BACKGROUND,
            SWT.COLOR_WIDGET_BORDER,
            SWT.COLOR_WIDGET_DARK_SHADOW,
            SWT.COLOR_WIDGET_DISABLED_FOREGROUND,
            SWT.COLOR_WIDGET_FOREGROUND,
            SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW,
            SWT.COLOR_WIDGET_LIGHT_SHADOW,
            SWT.COLOR_WIDGET_NORMAL_SHADOW,};
    static String [] colorNames = new String[] {"SWT.COLOR_INFO_BACKGROUND",
            "SWT.COLOR_INFO_FOREGROUND",
            "SWT.COLOR_LINK_FOREGROUND",
            "SWT.COLOR_LIST_BACKGROUND",
            "SWT.COLOR_LIST_FOREGROUND",
            "SWT.COLOR_LIST_SELECTION",
            "SWT.COLOR_LIST_SELECTION_TEXT",
            "SWT.COLOR_TEXT_DISABLED_BACKGROUND",
            "SWT.COLOR_TITLE_BACKGROUND",
            "SWT.COLOR_TITLE_BACKGROUND_GRADIENT",
            "SWT.COLOR_TITLE_FOREGROUND",
            "SWT.COLOR_TITLE_INACTIVE_BACKGROUND",
            "SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT",
            "SWT.COLOR_TITLE_INACTIVE_FOREGROUND",
            "SWT.COLOR_WIDGET_BACKGROUND",
            "SWT.COLOR_WIDGET_BORDER",
            "SWT.COLOR_WIDGET_DARK_SHADOW",
            "SWT.COLOR_WIDGET_DISABLED_FOREGROUND",
            "SWT.COLOR_WIDGET_FOREGROUND",
            "SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW",
            "SWT.COLOR_WIDGET_LIGHT_SHADOW",
            "SWT.COLOR_WIDGET_NORMAL_SHADOW",};
    @Test
    public void test_235_(){
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setText("The SWT.Settings Event");
        shell.setLayout(new GridLayout());
        Label label = new Label(shell, SWT.WRAP);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        label.setText("Change a system setting and the table below will be updated.");
        final Table table = new Table(shell, SWT.BORDER);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        TableColumn column = new TableColumn(table, SWT.NONE);
        column = new TableColumn(table, SWT.NONE);
        column.setWidth(150);
        column = new TableColumn(table, SWT.NONE);
        for (int i = 0; i < colorIds.length; i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            Color color = display.getSystemColor(colorIds[i]);
            item.setText(0, colorNames[i]);
            item.setBackground(1, color);
            item.setText(2, color.toString());
        }
        TableColumn[] columns = table.getColumns();
        columns[0].pack();
        columns[2].pack();
        display.addListener(SWT.Settings, event -> {
            for (int i = 0; i < colorIds.length; i++) {
                Color color = display.getSystemColor(colorIds[i]);
                TableItem item = table.getItem(i);
                item.setBackground(1, color);
            }
            TableColumn[] columns1 = table.getColumns();
            columns1[0].pack();
            columns1[2].pack();
        });

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }


    @Test
    public void test_7_(){
        final Display display = new Display ();
        final Image image = new Image (display, 16, 16);
        GC gc = new GC (image);
        gc.setBackground (display.getSystemColor (SWT.COLOR_RED));
        gc.fillRectangle (image.getBounds ());
        gc.dispose ();
        final Shell shell = new Shell (display);
        shell.setText ("Lazy Table");
        shell.setLayout (new FillLayout ());
        final Table table = new Table (shell, SWT.BORDER | SWT.MULTI);
        table.setSize (200, 200);
        Thread thread = new Thread () {
            @Override
            public void run () {
                for (int i=0; i<20000; i++) {
                    if (table.isDisposed ()) return;
                    final int [] index = new int [] {i};
                    display.syncExec (() -> {
                        if (table.isDisposed ()) return;
                        TableItem item = new TableItem (table, SWT.NONE);
                        item.setText ("Table Item " + index [0]);
                        item.setImage (image);
                    });
                }
            }
        };
        thread.start ();
        shell.setSize (200, 200);
        shell.open ();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        image.dispose ();
        display.dispose ();
    }

    /**设置字体*/
    @Test
    public void test_100_(){
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Snippet 100");
        shell.setBounds(10, 10, 200, 200);
        Text text = new Text(shell, SWT.MULTI);
        Rectangle clientArea = shell.getClientArea();
        text.setBounds(clientArea.x + 10, clientArea.y + 10, 150, 150);
        Font initialFont = text.getFont();
        FontData[] fontData = initialFont.getFontData();
        for (FontData element : fontData) {
            element.setHeight(24);
        }
        Font newFont = new Font(display, fontData);
        text.setFont(newFont);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        newFont.dispose();
        display.dispose();
    }


    /**
     * 主Shell打开副Shell并关闭主Shell
     */
    @Test
    public void test_106_(){
        final Display display = new Display();
        final int [] count = new int [] {4};
        final Image image = new Image(display, 300, 300);
        GC gc = new GC(image);
        gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
        gc.fillRectangle(image.getBounds());
        gc.drawText("Splash Screen", 10, 10);
        gc.dispose();
        final Shell splash = new Shell(SWT.ON_TOP);
        final ProgressBar bar = new ProgressBar(splash, SWT.NONE);
        bar.setMaximum(count[0]);
        Label label = new Label(splash, SWT.NONE);
        label.setImage(image);
        FormLayout layout = new FormLayout();
        splash.setLayout(layout);
        FormData labelData = new FormData ();
        labelData.right = new FormAttachment (100, 0);
        labelData.bottom = new FormAttachment (100, 0);
        label.setLayoutData(labelData);
        FormData progressData = new FormData ();
        progressData.left = new FormAttachment (0, 5);
        progressData.right = new FormAttachment (100, -5);
        progressData.bottom = new FormAttachment (100, -5);
        bar.setLayoutData(progressData);
        splash.pack();
        Rectangle splashRect = splash.getBounds();
        Rectangle displayRect = display.getBounds();
        int x = (displayRect.width - splashRect.width) / 2;
        int y = (displayRect.height - splashRect.height) / 2;
        splash.setLocation(x, y);
        splash.open();
        display.asyncExec(() -> {
            Shell [] shells = new Shell[count[0]];
            for (int i1=0; i1<count[0]; i1++) {
                shells [i1] = new Shell(display);
                shells [i1].setSize (300, 300);
                shells [i1].addListener(SWT.Close, e -> --count[0]);
                bar.setSelection(i1+1);
                try {Thread.sleep(1000);} catch (Throwable e) {}
            }
            splash.close();
            image.dispose();
            for (int i2=0; i2<count[0]; i2++) {
                shells [i2].setText("SWT Snippet 104 - " + (i2+1));
                shells [i2].open();
            }
        });
        while (count [0] != 0) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose();
    }

}
