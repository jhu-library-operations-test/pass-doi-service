# pass-doi-service
Service for accepting a DOI and returning a Journal ID and Crossref metadata for the DOI

## Description
This service accepts a journal DOI as a query parameter:

`http://<host>:<port>/journal?doi=<doi>`

DOIs must contain a form like `10.1234/ ...`
If a DOI is of a longer URL form containing the string `doi.org/`, then we truncate the DOI to take everything after this
substring.

The service validates the form of the doi - if it is valid, then we hit the Crossref API to get 
information about the corresponding journal. We then check to see if there is a 
`Journal` object in PASS for this journal. If not we create one. The service then 
returns to the caller a JSON object containing the `journal-id` of the PASS journal, 
and a `crossref` object representing the data returned to the service as a result of the Crossref call.

