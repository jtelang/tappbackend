import grails.converters.JSON
import groovy.sql.Sql
import tapp.Products
import tapp.PurchasedProducts
import tapp.RetailersMapping
import tapp.Users

class AlbumsController {

    def dataSource

    def index = {
        render "action: list, input parameters:  userid & title & artist & genres"
    }

    def list = {
        def db = new Sql(dataSource)
        def userid = request.JSON.userid
        def title = request.JSON.title
        def artist = request.JSON.artist
        def genres = request.JSON.genres

        def responseData = []
        def data = []
        if (userid) {
            try {
                def selectedUser = Users.get(Integer.parseInt(userid))
                String sqlString = "select ProductID, ProductTitle, AuthorTitle, GenresTitle from TblProducts where ProductType='album'";
                if (title) {
                    sqlString = sqlString + "  and ProductTitle like '%" + title + "%'"
                }
                if (artist) {
                    sqlString = sqlString + "  and AuthorTitle like '%" + artist + "%'"
                }
                if (genres) {
                    sqlString = sqlString + "  and GenresTitle like '%" + genres + "%'"
                }

                db.eachRow(sqlString) {
                    def pid = it.ProductID
                    def recommendInstance = null
                    def recommendObj = PurchasedProducts.createCriteria()
                    recommendInstance = recommendObj.get {
                        and {
                            eq("userid", selectedUser)
                            eq("productid.id", (int) pid)
                        }
                        maxResults(1)
                    }

                    def stores = []
                    def retailerObj = RetailersMapping.findAllByProductid(Products.get((int) pid))
                    retailerObj.each { row1 ->
                        stores << [retailerid: row1.retailerid.id, retailertitle: row1.retailerid.retailertitle, retailerprice: row1.price]
                    }

                    data << [id: it.ProductID, title: it.ProductTitle, artist: it.AuthorTitle, genres: it.GenresTitle, purchased: recommendInstance ? 1 : 0, recommended: recommendInstance?.recommended ? 1 : 0, credit: selectedUser.currentcredit, store: stores]
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
            } catch (Exception e) {
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
