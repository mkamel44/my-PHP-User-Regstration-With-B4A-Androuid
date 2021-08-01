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

public class register extends Activity implements B4AActivity{
	public static register mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "my.computerise.demo", "my.computerise.demo.register");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (register).");
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
		activityBA = new BA(this, layout, processBA, "my.computerise.demo", "my.computerise.demo.register");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "my.computerise.demo.register", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (register) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (register) Resume **");
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
		return register.class;
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
            BA.LogInfo("** Activity (register) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (register) Pause event (activity is not paused). **");
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
            register mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (register) Resume **");
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
public anywheresoftware.b4a.objects.EditTextWrapper _txtuserid = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtpassword = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtfullname = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtlocation = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtemail = null;
public my.computerise.demo.main _main = null;
public my.computerise.demo.login _login = null;
public my.computerise.demo.reset _reset = null;
public my.computerise.demo.member _member = null;
public my.computerise.demo.change _change = null;
public my.computerise.demo.httputils2service _httputils2service = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 19;BA.debugLine="Activity.LoadLayout(\"frmRegister\")";
mostCurrent._activity.LoadLayout("frmRegister",mostCurrent.activityBA);
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _btnregister_click() throws Exception{
String _struserid = "";
String _strpassword = "";
String _strfullname = "";
String _strlocation = "";
String _stremail = "";
my.computerise.demo.httpjob _job1 = null;
 //BA.debugLineNum = 30;BA.debugLine="Sub btnRegister_Click";
 //BA.debugLineNum = 31;BA.debugLine="Dim strUserID As String = txtUserID.Text.Trim";
_struserid = mostCurrent._txtuserid.getText().trim();
 //BA.debugLineNum = 32;BA.debugLine="If strUserID = \"\" Then";
if ((_struserid).equals("")) { 
 //BA.debugLineNum = 33;BA.debugLine="Msgbox(\"Please enter User ID\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please enter User ID"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 34;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 36;BA.debugLine="Dim strPassword As String = txtPassword.Text.Trim";
_strpassword = mostCurrent._txtpassword.getText().trim();
 //BA.debugLineNum = 37;BA.debugLine="If strPassword = \"\" Then";
if ((_strpassword).equals("")) { 
 //BA.debugLineNum = 38;BA.debugLine="Msgbox(\"Please enter Password\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please enter Password"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 39;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 41;BA.debugLine="Dim strFullName As String = txtFullName.Text.Trim";
_strfullname = mostCurrent._txtfullname.getText().trim();
 //BA.debugLineNum = 42;BA.debugLine="If strFullName = \"\" Then";
if ((_strfullname).equals("")) { 
 //BA.debugLineNum = 43;BA.debugLine="Msgbox(\"Please enter Full Name\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please enter Full Name"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 44;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 46;BA.debugLine="Dim strLocation As String = txtLocation.Text.Trim";
_strlocation = mostCurrent._txtlocation.getText().trim();
 //BA.debugLineNum = 47;BA.debugLine="If strLocation = \"\" Then";
if ((_strlocation).equals("")) { 
 //BA.debugLineNum = 48;BA.debugLine="Msgbox(\"Please enter Location\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please enter Location"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 49;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 51;BA.debugLine="Dim strEmail As String = txtEmail.Text.Trim";
_stremail = mostCurrent._txtemail.getText().trim();
 //BA.debugLineNum = 52;BA.debugLine="If strEmail = \"\" Then";
if ((_stremail).equals("")) { 
 //BA.debugLineNum = 53;BA.debugLine="Msgbox(\"Please enter Email\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please enter Email"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 54;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 56;BA.debugLine="If Validate_Email(strEmail) = False Then";
if (_validate_email(_stremail)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 57;BA.debugLine="Msgbox(\"Email format is incorrect\", \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Email format is incorrect"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 //BA.debugLineNum = 58;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 61;BA.debugLine="Dim Job1 As HttpJob";
_job1 = new my.computerise.demo.httpjob();
 //BA.debugLineNum = 62;BA.debugLine="Job1.Initialize(\"Register\", Me)";
_job1._initialize /*String*/ (processBA,"Register",register.getObject());
 //BA.debugLineNum = 63;BA.debugLine="Job1.Download2(Main.strURL & \"signup.php\", _";
_job1._download2 /*String*/ (mostCurrent._main._strurl /*String*/ +"signup.php",new String[]{"Action","Register","UserID",mostCurrent._txtuserid.getText(),"Password",mostCurrent._txtpassword.getText(),"FullName",mostCurrent._txtfullname.getText(),"Location",mostCurrent._txtlocation.getText(),"Email",mostCurrent._txtemail.getText()});
 //BA.debugLineNum = 70;BA.debugLine="ProgressDialogShow(\"Connecting to server...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Connecting to server..."));
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 11;BA.debugLine="Dim txtUserID As EditText";
mostCurrent._txtuserid = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Dim txtPassword As EditText";
mostCurrent._txtpassword = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim txtFullName As EditText";
mostCurrent._txtfullname = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim txtLocation As EditText";
mostCurrent._txtlocation = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim txtEmail As EditText";
mostCurrent._txtemail = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(my.computerise.demo.httpjob _job) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
String _res = "";
String _action = "";
 //BA.debugLineNum = 73;BA.debugLine="Sub JobDone (Job As HttpJob)";
 //BA.debugLineNum = 74;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 75;BA.debugLine="If Job.Success Then";
if (_job._success /*boolean*/ ) { 
 //BA.debugLineNum = 76;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 77;BA.debugLine="Dim res As String";
_res = "";
 //BA.debugLineNum = 78;BA.debugLine="Dim action As String";
_action = "";
 //BA.debugLineNum = 79;BA.debugLine="res = Job.GetString";
_res = _job._getstring /*String*/ ();
 //BA.debugLineNum = 80;BA.debugLine="parser.Initialize(res)";
_parser.Initialize(_res);
 //BA.debugLineNum = 81;BA.debugLine="Select Job.JobName";
switch (BA.switchObjectToInt(_job._jobname /*String*/ ,"Register")) {
case 0: {
 //BA.debugLineNum = 83;BA.debugLine="action = parser.NextValue";
_action = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 84;BA.debugLine="If action = \"Mail\" Then";
if ((_action).equals("Mail")) { 
 //BA.debugLineNum = 85;BA.debugLine="Msgbox(\"An email was sent to \"";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("An email was sent to "+mostCurrent._txtemail.getText()+". Please click on the link to finish registration"),BA.ObjectToCharSequence("Registration"),mostCurrent.activityBA);
 //BA.debugLineNum = 86;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else if((_action).equals("MailInUse")) { 
 //BA.debugLineNum = 88;BA.debugLine="Msgbox(\"The user id '\" & txtUs";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("The user id '"+mostCurrent._txtuserid.getText()+"' or email ("+mostCurrent._txtemail.getText()+") is already in used"),BA.ObjectToCharSequence("Registration"),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 90;BA.debugLine="Msgbox(\"Server does not return expected resul";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Server does not return expected result."),BA.ObjectToCharSequence("Registration"),mostCurrent.activityBA);
 };
 break; }
}
;
 }else {
 //BA.debugLineNum = 95;BA.debugLine="ToastMessageShow(\"Error: \" & Job.ErrorMessage, T";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error: "+_job._errormessage /*String*/ ),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 97;BA.debugLine="Job.Release";
_job._release /*String*/ ();
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static boolean  _validate_email(String _emailaddress) throws Exception{
anywheresoftware.b4a.keywords.Regex.MatcherWrapper _matchemail = null;
 //BA.debugLineNum = 101;BA.debugLine="Sub Validate_Email(EmailAddress As String) As Bool";
 //BA.debugLineNum = 102;BA.debugLine="Dim MatchEmail As Matcher = Regex.Matcher(\"^(?";
_matchemail = new anywheresoftware.b4a.keywords.Regex.MatcherWrapper();
_matchemail = anywheresoftware.b4a.keywords.Common.Regex.Matcher("^(?i)[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])$",_emailaddress);
 //BA.debugLineNum = 104;BA.debugLine="If MatchEmail.Find = True Then";
if (_matchemail.Find()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 106;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 109;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return false;
}
}
