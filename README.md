Currency Exchange 
=================

#Assumptions
1. The time (with field: timePlaced) in the POST request is in UTC and in the format of the example: 24-JAN-15 10:27:44
2. All the fields are mandatory and can't be empty. For flexibility in this exercise, no validation on the length of the fields is done (e.g. no validation that the currencies are in a list of supported currencies).


#API Endpoint
1. Returns only an HTTP status code. Main status codes:
  * 201 if the trade is correctly received.
  * 400 if the format of the request is not correct.
  * 500 if there is an internal error (e.g. The Database is not available).

