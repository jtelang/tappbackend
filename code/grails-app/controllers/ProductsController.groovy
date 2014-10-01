import grails.converters.JSON
import groovy.sql.Sql
import tapp.CreditHistory
import tapp.Followers
import tapp.Products
import tapp.PurchasedProducts
import tapp.Retailers
import tapp.RetailersMapping
import tapp.UserProfiles
import tapp.Users

class ProductsController {

    def dataSource
    def androidGcmService

    def index = {
        render "action: buy, input parameters:  userid & productid & retailerid & recommendedby" +
                "<br/>" +
                "action: credit, input parameters:  userid" +
                "<br/>" +
                "action: recommend, input parameters:  userid & productid"
    }

    def buy = {
        def userid = request.JSON.userid
        def productid = request.JSON.productid
        def retailerid = request.JSON.retailerid
        def recommendedby = request.JSON.recommendedby
        def useavailablecredit = request.JSON.useavailablecredit

        def responseData = []
        def commission = 0.10

        if (userid && productid && retailerid && useavailablecredit) {
            try {
                def selectedUser = Users.get(Integer.parseInt(userid))
                def selectedUserProfile = UserProfiles.findByUserid(selectedUser)
                def productInstance = Products.get(Integer.parseInt(productid))
                def retailerInstance = Retailers.get(Integer.parseInt(retailerid))

                if (selectedUser && productInstance && retailerInstance) {
                    def retailersMapping = RetailersMapping.createCriteria()
                    def rMappingInstance = retailersMapping.get {
                        and {
                            eq("retailerid", retailerInstance)
                            eq("productid", productInstance)
                        }
                    }

                    if (selectedUser.currentcredit > 0 && Integer.parseInt(useavailablecredit) == 1) {
                        if (selectedUser.currentcredit >= rMappingInstance.price) {
                            selectedUser.currentcredit = selectedUser.currentcredit - rMappingInstance.price
                        } else {
                            selectedUser.currentcredit = 0
                        }
                        selectedUser.save(false)
                    }

                    def purchasedObj = new PurchasedProducts()
                    purchasedObj.userid = selectedUser
                    purchasedObj.fullname = selectedUserProfile?.fullname
                    purchasedObj.productid = productInstance
                    purchasedObj.title = productInstance.producttitle
                    purchasedObj.retailerid = retailerInstance
                    purchasedObj.retailertitle = retailerInstance.retailertitle
                    purchasedObj.amount = rMappingInstance.price
                    purchasedObj.purchaseddate = new Date()
                    purchasedObj.expirydate = new Date() + 7
                    purchasedObj.save(false)

                    if (recommendedby) {
                        def recommendedUser = Users.get(Integer.parseInt(recommendedby))
                        def recommendedUserProfile = UserProfiles.findByUserid(recommendedUser)

                        if (rMappingInstance.price > 0) {
                            def creditObj = new CreditHistory()
                            creditObj.purchasedid = purchasedObj
                            creditObj.userid = recommendedUser
                            creditObj.fullname = recommendedUserProfile?.fullname
                            creditObj.purchasedbyid = selectedUser
                            creditObj.purchasedbyname = selectedUserProfile?.fullname
                            creditObj.retailerid = retailerInstance
                            creditObj.retailertitle = retailerInstance.retailertitle
                            creditObj.amount = rMappingInstance.price
                            creditObj.commission = commission
                            creditObj.save(false)

//                            def purchasedMapping = PurchasedProducts.createCriteria()
//                            def purchasedInstance = purchasedMapping.get {
//                                and {
//                                    eq("userid", selectedUser)
//                                    eq("productid", productInstance)
//                                    eq("amount", Double.parseDouble("0.00"))
//                                }
//                                maxResults(1)
//                            }
//
//                            if (purchasedInstance) {
//                                recommendedUser.pendingcredit = recommendedUser.pendingcredit - commission
//                                recommendedUser.currentcredit = recommendedUser.currentcredit + commission
//                            } else
                            recommendedUser.currentcredit = recommendedUser.currentcredit + commission
                        } else
                            recommendedUser.pendingcredit = recommendedUser.pendingcredit + commission

                        recommendedUser.save(false)

                        if (recommendedUser.disabled == 0) {
                            def deviceToken = []
                            deviceToken << recommendedUser.devicetoken
                            //def message = ["message": "Thanks for your recommendation to buy " + productInstance?.producttype?.toString() + ": " + productInstance.producttitle + " \n From: "+selectedUser.countrycode+selectedUser.phonenumber, "countrycode": selectedUser.countrycode, "phonenumber": selectedUser.phonenumber]
                            def message = ["message": '{"message":"Thanks for your recommendation to buy ' + productInstance?.producttype?.toString() + ': ' + productInstance.producttitle + ' From: ' + selectedUser.countrycode + selectedUser.phonenumber + '", "countrycode": "' + selectedUser.countrycode + '", "phonenumber": "' + selectedUser.phonenumber + '"}']
                            androidGcmService.sendMessage(message, deviceToken, "", grailsApplication.config.android.gcm.api.key)
                        }
                    }

                    responseData = [
                            'status' : 0,
                            'message': "success",
                    ]
                } else {
                    responseData = [
                            'status' : 2,
                            'message': "no data",
                    ]
                }
            }
            catch (Exception e) {
                responseData = [
                        'status' : 1,
                        'message': "error",
                        'result' : e.toString(),
                ]
            }
        } else {
            responseData = [
                    'status' : 3,
                    'message': "missing input parameters",
            ]
        }

        render responseData as JSON
    }

    def credit = {
        def db = new Sql(dataSource)
        def userid = request.JSON.userid

        def responseData = []
        def data = []

        if (userid) {
            try {
                def userInstance = Users.get(Integer.parseInt(userid))
                String sqlString = "select PurchasedByID, PurchasedByName, RetailerTitle, Amount, Commission, DATE_FORMAT(PurchasedDate,'%d %b %Y') as PurchasedDate from TblCreditHistory Where UserID = " + userid + " ORDER BY PurchasedDate DESC;";
                db.eachRow(sqlString) {
                    def currentUser = Users.get((int) it.PurchasedByID)
                    data << [username: it.PurchasedByName, "countrycode": currentUser.countrycode, "phonenumber": currentUser.phonenumber, place: it.RetailerTitle, purchaseddate: it.PurchasedDate, price: it.Amount, commission: it.Commission]
                }

                responseData = [
                        'status' : 0,
                        'message': "success",
                        'credit' : userInstance?.currentcredit,
                        'pending': userInstance?.pendingcredit,
                        'result' : data,
                ]
            }
            catch (Exception e) {
                responseData = [
                        'status' : 1,
                        'message': "error",
                        'result' : e.toString(),
                ]
            }
        } else {
            responseData = [
                    'status' : 3,
                    'message': "missing input parameters",
            ]
        }
        render responseData as JSON
    }

    def recommend = {
        def userid = request.JSON.userid
        def productid = request.JSON.productid

        def responseData = []
        if (userid && productid) {

            try {
                def selectedUser = Users.get(Integer.parseInt(userid))
                def productInstance = Products.get(Integer.parseInt(productid))

                def recommendObj = PurchasedProducts.createCriteria()
                def recommendInstance = recommendObj.get {
                    and {
                        eq("userid", selectedUser)
                        eq("productid", productInstance)
                    }
                    maxResults(1)
                }

                if (selectedUser && productInstance && recommendInstance) {
                    recommendInstance?.recommended = 1
                    recommendInstance?.save(false)

                    def deviceToken = []
                    def followerInstance = Followers.findAllByUserid(selectedUser)
                    followerInstance.each {
                        if (it.followerid.disabled == 0)
                            deviceToken << it.followerid.devicetoken
                    }

                    if (deviceToken) {
                        //def message = ["message": "You have one recommendation from " + selectedUser.countrycode+selectedUser.phonenumber + " \n " + productInstance?.producttype?.toString() + ": " + productInstance?.producttitle, "countrycode": selectedUser.countrycode, "phonenumber": selectedUser.phonenumber]
                        def message = ["message": '{"message": "You have one recommendation from ' + selectedUser.countrycode + selectedUser.phonenumber + ' ' + productInstance?.producttype?.toString() + ': ' + productInstance?.producttitle + '", "countrycode": "' + selectedUser.countrycode + '", "phonenumber": "' + selectedUser.phonenumber + '"}']
                        androidGcmService.sendMessage(message, deviceToken, "", grailsApplication.config.android.gcm.api.key)
                    }

                    responseData = [
                            'status' : 0,
                            'message': "success",
                    ]
                } else {
                    responseData = [
                            'status' : 2,
                            'message': "no data",
                    ]
                }
            }
            catch (Exception e) {
                responseData = [
                        'status' : 1,
                        'message': "error",
                        'result' : e.toString(),
                ]
            }
        } else {
            responseData = [
                    'status' : 3,
                    'message': "missing input parameters",
            ]
        }

        render responseData as JSON
    }
}
