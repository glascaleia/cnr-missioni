package it.cnr.missioni.dashboard;

import java.util.Locale;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import it.cnr.missioni.dashboard.action.LoginAction;
import it.cnr.missioni.dashboard.action.RegistrationUserAction;
import it.cnr.missioni.dashboard.action.UpdateUserAction;
import it.cnr.missioni.dashboard.action.VeicoloAction;
import it.cnr.missioni.dashboard.event.DashboardEventBus;
import it.cnr.missioni.dashboard.event.DashboardEvent.BrowserResizeEvent;
import it.cnr.missioni.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import it.cnr.missioni.dashboard.event.DashboardEvent.UserLoggedOutEvent;
import it.cnr.missioni.dashboard.view.LoginView;
import it.cnr.missioni.dashboard.view.MainView;
import it.cnr.missioni.model.user.User;

@Theme("dashboard")
@Widgetset("it.cnr.missioni.dashboard.DashboardWidgetSet")
@Title("Missioni Dashboard")
@SuppressWarnings("serial")
public final class DashboardUI extends UI {

	/*
	 * This field stores an access to the dummy backend layer. In real
	 * applications you most likely gain access to your beans trough lookup or
	 * injection; and not in the UI but somewhere closer to where they're
	 * actually accessed.
	 */
	private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

	@Override
	protected void init(final VaadinRequest request) {
		setLocale(Locale.US);

		DashboardEventBus.register(this);
		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);

		updateContent();

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				DashboardEventBus.post(new BrowserResizeEvent());
			}
		});
	}

	/**
	 * Updates the correct content for this UI based on the current user status.
	 * If the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	private void updateContent() {
		User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		// if (user != null && "admin".equals(user.getRole())) {
		if (user != null) {
			// Authenticated user
			setContent(new MainView());
			removeStyleName("loginview");
			getNavigator().navigateTo(getNavigator().getState());
		} else {
			setContent(new LoginView());
			addStyleName("loginview");
		}
	}

	@Subscribe
	public void userLoginRequested(final LoginAction loginRequest) {

		if (loginRequest.doAction()) {
			VaadinSession.getCurrent().setAttribute(User.class.getName(), loginRequest.getUser());
			updateContent();
		}

	}

	@Subscribe
	public void userRegistrationRequested(final RegistrationUserAction registrationUserAction) {
		registrationUserAction.doAction();
	}
	
	@Subscribe
	public void userUpdateUserRequested(final UpdateUserAction updateUserAction) {
		updateUserAction.doAction();
	}
	
	@Subscribe
	public void veicoloRequested(final VeicoloAction veicoloAction) {
		veicoloAction.doAction();
	}

	@Subscribe
	public void userLoggedOut(final UserLoggedOutEvent event) {
		// When the user logs out, current VaadinSession gets closed and the
		// page gets reloaded on the login screen. Do notice the this doesn't
		// invalidate the current HttpSession.
		VaadinSession.getCurrent().close();
		Page.getCurrent().reload();
	}

	@Subscribe
	public void closeOpenWindows(final CloseOpenWindowsEvent event) {
		for (Window window : getWindows()) {
			window.close();
		}
	}

	public static DashboardEventBus getDashboardEventbus() {
		return ((DashboardUI) getCurrent()).dashboardEventbus;
	}
}