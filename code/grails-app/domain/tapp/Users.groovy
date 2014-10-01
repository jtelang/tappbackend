package tapp

class Users {

    Integer id
    String countrycode
    String phonenumber
    Double currentcredit = 0
    Double pendingcredit = 0
    Date createddate = new Date()
    String status
    String  devicetoken
    String  deviceid
    Integer disabled = 0

    static mapping = {
        table 'TblUsers'
        version false
        id generator: 'identity', column: 'UserID', index: 'UserID'
        columns {
            countrycode column: 'CountryCode', index: 'CountryCode'
            phonenumber column: 'PhoneNumber', index: 'PhoneNumber'
            currentcredit column: 'CurrentCredit'
            pendingcredit column: 'PendingCredit'
            createddate column: 'CreatedDate'
            status column: 'Status'
            devicetoken column: 'DeviceToken'
            deviceid column: 'DeviceID'
            disabled column: 'Disabled'
            devicetoken sqlType: 'text'
        }
    }

    static constraints = {
        phonenumber(nullable: false)
    }
}
