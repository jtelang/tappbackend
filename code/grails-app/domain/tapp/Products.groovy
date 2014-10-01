package tapp

class Products {

    Integer id
    String producttitle
    String authortitle
    String albumtitle
    String genrestitle
    ProductTypes producttype = ProductTypes.song
    Categories category = Categories.none

    static mapping = {
        table 'TblProducts'
        version false
        id generator: 'identity', column: 'ProductID', index: 'ProductID'
        columns {
            producttitle column: 'ProductTitle'
            producttype column: 'ProductType'
            authortitle column: 'AuthorTitle'
            authortitle column: 'AlbumTitle'
            genrestitle column: 'GenresTitle'
            category column: 'Category'
        }
    }

    static constraints = {
    }

    enum ProductTypes {
        album, song, game
    }

    enum Categories {
        none, newcategory, popularcategory
    }
}
