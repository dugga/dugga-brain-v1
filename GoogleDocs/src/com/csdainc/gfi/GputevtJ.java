package com.csdainc.gfi;

import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.When;

public class GputevtJ {

	public static void main(String[] args) {

		String account = args[0];
		String password = args[1];
		String description = args[2];
		String strDt = args[3];
		String strTm = args[4];
		String endDt = args[5];
		String endTm = args[6];     

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
			myEntry.setTitle(new PlainTextConstruct(description));
			
			// set times
			DateTime startTime = new DateTime(startCalendar.getTimeInMillis(), Calendar.ZONE_OFFSET);
			DateTime endTime = new DateTime(endCalendar.getTimeInMillis(), Calendar.ZONE_OFFSET);
			When eventTimes = new When();
			eventTimes.setStartTime(startTime);
			eventTimes.setEndTime(endTime);
			myEntry.addTime(eventTimes);

			// send message
			System.out.println("*** Adding Event ***" + account);
			System.out.println("account: " + account);
			System.out.println("description: " + description);
			System.out.println("Starting: " + startTime.toString());
			System.out.println("Ending: " + endTime.toString());
			
			// Send the request and receive the response:
			calendarService.insert(calendarUrl, myEntry);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}


	}


}