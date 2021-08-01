package my.computerise.demo;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class change extends Activity implements B4AActivity{
	public static change mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "my.computerise.demo", "my.computerise.demo.change");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (change).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "my.computerise.demo", "my.computerise.demo.change");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "my.computerise.demo.change", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (change) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (change) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return change.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (change) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (change) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            change mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (change) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtemail = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtpassword1 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtpassword2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtpassword3 = null;
public my.computerise.demo.main _main = null;
public my.computerise.demo.register _register = null;
public my.computerise.demo.login _login = null;
public my.computerise.demo.reset _reset = null;
public my.computerise.demo.member _member = null;
public my.computerise.demo.httputils2service _httputils2service = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 18;BA.debugLine="Activity.LoadLayout(\"frmChange\")";
mostCurrent._activity.LoadLayout("frmChange",mostCurrent.activityBA);
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _btnsubmit_click() throws Exception{
String _stremail = "";
String _strpassword1 = "";
String _strpassword2 = "";
String _strpassword3 = "";
my.computerise.demo.httpjob _job6 = null;
 //BA.debugLineNum = 29;BA.debugLine="Sub btnSubmit_Click";
 //BA.debugLineNum = 30;BA.debugLine="Dim strEmail As String = txtEmail.Text.Trim";
_stremail = mostCurrent._txtemail.getText().trim();
 //BA.debugLineNum = 31;BA.debugLine="If strEmail = \"\" Then";
if ((_stremail).equals("")) { 
 //BA.debugLineNum = 32;BA.debugLine="Msgbox(\"Please enter Your Email\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please enter Your Email"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 33;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 35;BA.debugLine="Dim strPassword1 As String = txtPassword1.Text.Tr";
_strpassword1 = mostCurrent._txtpassword1.getText().trim();
 //BA.debugLineNum = 36;BA.debugLine="If strPassword1 = \"\" Then";
if ((_strpassword1).equals("")) { 
 //BA.debugLineNum = 37;BA.debugLine="Msgbox(\"Please enter Current Password\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please enter Current Password"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 38;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 40;BA.debugLine="Dim strPassword2 As String = txtPassword2.Text.Tr";
_strpassword2 = mostCurrent._txtpassword2.getText().trim();
 //BA.debugLineNum = 41;BA.debugLine="If strPassword2 = \"\" Then";
if ((_strpassword2).equals("")) { 
 //BA.debugLineNum = 42;BA.debugLine="Msgbox(\"Please enter New Password\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please enter New Password"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 43;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 45;BA.debugLine="Dim strPassword3 As String = txtPassword3.Text.Tr";
_strpassword3 = mostCurrent._txtpassword3.getText().trim();
 //BA.debugLineNum = 46;BA.debugLine="If strPassword3 = \"\" Then";
if ((_strpassword3).equals("")) { 
 //BA.debugLineNum = 47;BA.debugLine="Msgbox(\"Please reenter New Password\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please reenter New Password"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 48;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 50;BA.debugLine="If strPassword2 <> strPassword3 Then";
if ((_strpassword2).equals(_strpassword3) == false) { 
 //BA.debugLineNum = 51;BA.debugLine="Msgbox(\"New Password not match\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("New Password not match"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 52;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 55;BA.debugLine="Dim Job6 As HttpJob";
_job6 = new my.computerise.demo.httpjob();
 //BA.debugLineNum = 56;BA.debugLine="Job6.Initialize(\"Change\", Me)";
_job6._initialize /*String*/ (processBA,"Change",change.getObject());
 //BA.debugLineNum = 57;BA.debugLine="Job6.Download2(Main.strURL & \"change-password.php";
_job6._download2 /*String*/ (mostCurrent._main._strurl /*String*/ +"change-password.php",new String[]{"Email",_stremail,"Password1",_strpassword1,"Password2",_strpassword2});
 //BA.debugLineNum = 61;BA.debugLine="ProgressDialogShow(\"Connecting to server...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Connecting to server..."));
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 11;BA.debugLine="Private txtEmail As EditText";
mostCurrent._txtemail = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Private txtPassword1 As EditText";
mostCurrent._txtpassword1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Private txtPassword2 As EditText";
mostCurrent._txtpassword2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Private txtPassword3 As EditText";
mostCurrent._txtpassword3 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(my.computerise.demo.httpjob _job) throws Exception{
String _ret = "";
String _act = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
 //BA.debugLineNum = 64;BA.debugLine="Sub JobDone (Job As HttpJob)";
 //BA.debugLineNum = 65;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 66;BA.debugLine="If Job.Success = True Then";
if (_job._success /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 67;BA.debugLine="Dim ret As String";
_ret = "";
 //BA.debugLineNum = 68;BA.debugLine="Dim act As String";
_act = "";
 //BA.debugLineNum = 69;BA.debugLine="ret = Job.GetString";
_ret = _job._getstring /*String*/ ();
 //BA.debugLineNum = 70;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 71;BA.debugLine="parser.Initialize(ret)";
_parser.Initialize(_ret);
 //BA.debugLineNum = 72;BA.debugLine="act = parser.NextValue";
_act = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 73;BA.debugLine="If act = \"Success\" Then";
if ((_act).equals("Success")) { 
 //BA.debugLineNum = 74;BA.debugLine="Msgbox(\"Password updated successfully!\", \"Chang";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Password updated successfully!"),BA.ObjectToCharSequence("Change Password"),mostCurrent.activityBA);
 }else if((_act).equals("Not Found")) { 
 //BA.debugLineNum = 76;BA.debugLine="Msgbox(\"Email or Password not correct!\", \"Chang";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Email or Password not correct!"),BA.ObjectToCharSequence("Change Password"),mostCurrent.activityBA);
 }else if((_act).equals("Error") || (_act).equals("Failed")) { 
 //BA.debugLineNum = 78;BA.debugLine="Msgbox(\"An error occured!\", \"Change Password\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("An error occured!"),BA.ObjectToCharSequence("Change Password"),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 80;BA.debugLine="Msgbox(\"Uncaught error!\", \"Change Password\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Uncaught error!"),BA.ObjectToCharSequence("Change Password"),mostCurrent.activityBA);
 };
 }else {
 //BA.debugLineNum = 84;BA.debugLine="ToastMessageShow(\"Error: \" & Job.ErrorMessage, T";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error: "+_job._errormessage /*String*/ ),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 86;BA.debugLine="Job.Release";
_job._release /*String*/ ();
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
}
