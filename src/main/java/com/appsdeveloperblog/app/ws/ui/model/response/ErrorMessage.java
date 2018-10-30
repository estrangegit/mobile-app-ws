package com.appsdeveloperblog.app.ws.ui.model.response;

import java.util.Date;

public class ErrorMessage {

  private Date timeStamp;
  private String message;

  public ErrorMessage() {}

  public ErrorMessage(final Date timeStamp, final String message) {
    this.timeStamp = timeStamp;
    this.message = message;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(final Date timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
