import grails.converters.JSON
import groovy.sql.Sql
import tapp.PurchasedProducts
import tapp.Users

class OffersController {
    def dataSource

    def index = {
        render "action: list, input parameters: userid"
    }

    def list = {
        def db = new Sql(dataSource)
        def userid = request.JSON.userid

        def responseData = []
        def data = []
        def followerids = 0

        if (userid) {
            try {
                def currentUser = Users.get(Integer.parseInt(userid))
                String sqlString = "select group_concat(UserID separator ',') as followerids from TblFollowers where FollowerID = " + userid;
                db.eachRow(sqlString) {
                    followerids = it.followerids
                }

                sqlString = "select ProductID, Title, Amount, UserID, FullName, DATE_FORMAT(PurchasedDate,'%d %b %Y') as OfferStartDate, DATE_FORMAT(DATE_ADD(ExpiryDate,INTERVAL 7 DAY),'%d %b %Y') as OfferExpireDate, RetailerID, RetailerTitle from TblPurchasedProducts where Recommended = 1 AND ExpiryDate > NOW() AND UserID in ("+followerids+") GROUP BY ProductID,UserID";
                db.eachRow(sqlString) {
                    def pid = it.ProductID
                    def recommendInstance = null
                    def recommendObj = PurchasedProducts.createCriteria()
                    recommendInstance = recommendObj.get {
                        and {
                            eq("userid.id", Integer.parseInt(userid))
                            eq("productid.id", (int) pid)
                        }
                        maxResults(1)
                    }

                    def userInstance = Users.get(it.UserID)
                    data << [productid: it.ProductID, title: it.Title, price: it.Amount, recommendedbyid: it.UserID, recommendedby: it.FullName, "countrycode": userInstance.countrycode, "phonenumber": userInstance.phonenumber, offerdate: it.OfferStartDate, expirydate: it.OfferExpireDate, credit: currentUser.currentcredit, retailerid: it.RetailerID, retailer: it.RetailerTitle, purchased: recommendInstance ? 1 : 0, recommended: recommendInstance?.recommended ? 1 : 0]
                }

                if (data.size() > 0) {
                    responseData = [
                            'status' : 0,
                            'message': "success",
                            'result' : data,
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
                        'result': e.toString(),
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
