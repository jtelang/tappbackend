import tapp.Users

import java.util.List;
import java.util.Map;

class AndroidGcmSenderController {

    def androidGcmService
	def grailsApplication
	
	def beforeInterceptor = {
		if (params.projectId) {
			params.tokens = Users.findAll().devicetoken
		}
	}
	
	def configureMessage = {
		params.apiKey = 'AIzaSyA9lBoarBvWfVh0_jpnTPVQMpnNzAEwNfI'//grailsApplication.config.android.gcm.api.key ?: ''
		render view:'index', model: params
	}
	
	def index = {
        render view:'index', model:params
    }
	
	def sendMessage = {
        ['deviceToken', 'messageKey', 'messageValue'].each {
            key -> params[key] = [params[key]].flatten().findAll { it }
        }
        def messages = params.messageKey.inject([:]) {
            currentMessages, currentKey ->
                currentMessages << [ (currentKey) : params.messageValue[currentMessages.size()]]
        }
        flash.message = 'received.message.response'
        flash.message = flash.message + [androidGcmService.sendMessage(messages, params.deviceToken, params.collapseKey, params.apiKey).toString()]
        redirect(action:'index', params: params)
	}
}
