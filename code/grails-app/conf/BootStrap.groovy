
class BootStrap {

    def init = { servletContext ->


	def gn1 = new tapp.Genres(genre:"Rock").save(failOnError:true)


	def gn2 = new tapp.Genres(genre:"Pop").save(failOnError:true)
	def gn3 = new tapp.Genres(genre:"Classical").save(failOnError:true)
	def gn4 = new tapp.Genres(genre:"Jazz").save(failOnError:true)

        def an1 = new tapp.Artists(name:"Michael Jackson").save(failOnError:true)
	def an2 = new tapp.Artists(name:"A R Rehman").save(failOnError:true)
	def an3 = new tapp.Artists(name:"Ravi Shankar").save(failOnError:true)
 	def an4 = new tapp.Artists(name:"A C Upadhyaya").save(failOnError:true)

	def am1 = new tapp.Albums(title:"Thriller", genres:gn1, artists:an1).save(failOnError:true)
	
	def sg1 = new tapp.Songs(title:"Billy Jean", artists:an1, albums:am1).save(failOnError:true)
	def sg2 = new tapp.Songs(title:"Haunted House", artists:an1, albums:am1).save(failOnError:true)
	def sg3 = new tapp.Songs(title:"Killer Thriller", artists:an1, albums:am1).save(failOnError:true)
	def sg4 = new tapp.Songs(title:"Fast Track", artists:an1, albums:am1).save(failOnError:true)


	def gn1_ = new tapp.Gametype(gametype:"Action").save(failOnError:true)
	def gn2_ = new tapp.Gametype(gametype:"Racing").save(failOnError:true)
	def gn3_ = new tapp.Gametype(gametype:"Arcade").save(failOnError:true)
	def gn4_ = new tapp.Gametype(gametype:"Puzzle").save(failOnError:true)
        def an1_ = new tapp.Creator(name:"Michael Jackson").save(failOnError:true)
	def an2_ = new tapp.Creator(name:"A R Rehman").save(failOnError:true)
	def an3_ = new tapp.Creator(name:"Ravi Shankar").save(failOnError:true)
 	def an4_ = new tapp.Creator(name:"A C Upadhyaya").save(failOnError:true)
	def am1_ = new tapp.Gamecollection(title:"Thriller", gametype:gn1_, creator:an1_).save(failOnError:true)
	def sg1_ = new tapp.Game(title:"Billy Jean",creator:an1_,gamecollection:am1_).save(failOnError:true)
	def sg2_ = new tapp.Game(title:"Haunted House",creator:an1_,gamecollection:am1_).save(failOnError:true)
	def sg3_ = new tapp.Game(title:"Killer Thriller",creator:an1_,gamecollection:am1_).save(failOnError:true)
	def sg4_ = new tapp.Game(title:"Fast Track", creator:an1_,gamecollection:am1_).save(failOnError:true)

	def us1 = new tapp.User(version:1, credit:0, phone:9167466253).save(failOnError:true)
	def us2 = new tapp.User(version:1, credit:0, phone:9323011616).save(failOnError:true)
	def us3 = new tapp.User(version:1, credit:0, phone:9999999999).save(failOnError:true)
	def pf1 = new tapp.Profile(version:1,fullName:"Full Name",age:21,bio:"Bio",homepage:"http://www.google.com",		email:"abc@abc.com",timezone:"IST",city:"Mumbai",user:us1).save(failOnError:true)
	
	def today = new Date()

	def mb1 = new tapp.MediaBuy(recommend:true, dateCreated:today, mediaId:1, mediaIdentifier:"song", store:"Amazon", user:us1).save(failOnError:true)

	def mb2 = new tapp.MediaBuy(recommend:true, dateCreated:today, mediaId:2, mediaIdentifier:"game", store:"Amazon", user:us1).save(failOnError:true)	
	def mb3 = new tapp.MediaBuy(recommend:true, dateCreated:today, mediaId:3, mediaIdentifier:"song", store:"iTunes", user:us1).save(failOnError:true)
	
		



    }
    def destroy = {
    }
}
