package com.csdainc.gfi;

import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.data.extensions.Reminder.Method;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.extensions.Where;

public class GA4BEvntJ {

	public static void main(String[] args) {

		String account = args[0];
		String password = args[1];
		String title = args[2];
		String description = args[3];
		String strDt = args[4];
		String strTm = args[5];
		String endDt = args[6];
		String endTm = args[7];    
		String reminder = args[8];
		String reminderMinutes = args[9];
		String location = args[10];

		try {
			// authenticate
			CalendarService calendarService = new CalendarService("com.csdainc-GFi-v1");
			calendarService.setUserCredentials(account, password);

			URL calendarUrl = new URL("https://www.google.com/calendar/feeds/default/private/full");

			
			// parse input values - start date
			int startYear = Integer.parseInt(strDt.substring(0,4));
			int startMonth = Integer.parseInt(strDt.substring(4,6))-1;
			int startDate = Integer.parseInt(strDt.substring(6,8));
			int startHour = Integer.parseInt(strTm.substring(0,2));
			int startMinute = Integer.parseInt(strTm.substring(2,4));
			int startSeconds = Integer.parseInt(strTm.substring(4,6));
			int minutes = Integer.parseInt(reminderMinutes);
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.clear();
			startCalendar.setTimeZone(TimeZone.getDefault());
			startCalendar.set(startYear, startMonth, startDate, startHour, startMinute, startSeconds);
			
			// parse input values - end date
			int endYear = Integer.parseInt(endDt.substring(0,4));
			int endMonth = Integer.parseInt(endDt.substring(4,6))-1;
			int endDate = Integer.parseInt(endDt.substring(6,8));
			int endHour = Integer.parseInt(endTm.substring(0,2));
			int endMinute = Integer.parseInt(endTm.substring(2,4));
			int endSeconds = Integer.parseInt(endTm.substring(4,6));
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.clear();
			endCalendar.setTimeZone(TimeZone.getDefault());
			endCalendar.set(endYear, endMonth, endDate, endHour, endMinute, endSeconds);
						
			// set title
			CalendarEventEntry myEntry = new CalendarEventEntry();
			myEntry.setTitle(new PlainTextConstruct(title));
			myEntry.setContent(new PlainTextConstruct(description));
			
			// set times
			DateTime startTime = new DateTime(startCalendar.getTimeInMillis(), Calendar.ZONE_OFFSET);
			DateTime endTime = new DateTime(endCalendar.getTimeInMillis(), Calendar.ZONE_OFFSET);
			When eventTimes = new When();
			eventTimes.setStartTime(startTime);
			eventTimes.setEndTime(endTime);
			myEntry.addTime(eventTimes);

			// set reminder
			if (reminder.equals("*YES")) {
				Reminder rem = new Reminder();
				rem.setMethod(Method.EMAIL);
				rem.setMinutes(minutes);
				myEntry.getReminder().add(rem);
			}
			
			// set location
			Where where = new Where();
			where.setValueString(location);
			myEntry.getLocations().add(where);
			
			// send message
			System.out.println("*** Adding Event ***" + account);
			System.out.println("account: " + account);
			System.out.println("title: " + title);
			System.out.println("description: " + description);
			System.out.println("Starting: " + startTime.toString());
			System.out.println("Ending: " + endTime.toString());
			System.out.println("reminder: " + reminder);
			System.out.println("reminder Minutes: " + minutes);
			System.out.println("location: " + location);
			
			// Send the request and receive the response:
			calendarService.insert(calendarUrl, myEntry);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}


	}


}