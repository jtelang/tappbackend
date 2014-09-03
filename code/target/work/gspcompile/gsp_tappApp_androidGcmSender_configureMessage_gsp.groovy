import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_tappApp_androidGcmSender_configureMessage_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/androidGcmSender/_configureMessage.gsp" }
public Object run() {
Writer out = getOut()
Writer expressionOut = getExpressionOut()
registerSitemeshPreprocessMode()
printHtmlPart(0)
invokeTag('message','g',1,['code':("sender.usage.message")],-1)
printHtmlPart(1)
createTagBody(1, {->
printHtmlPart(2)
invokeTag('hiddenField','g',3,['name':("projectId"),'value':(projectId)],-1)
printHtmlPart(3)
invokeTag('message','g',6,['code':("sender.form.deviceToken")],-1)
printHtmlPart(4)
for( token in ([deviceToken].flatten()) ) {
printHtmlPart(5)
invokeTag('select','g',11,['name':("deviceToken"),'from':(tokens),'value':(token),'noSelection':(['':'-'])],-1)
printHtmlPart(6)
}
printHtmlPart(7)
expressionOut.print(resource(dir:'images/skin',file:'add.png'))
printHtmlPart(8)
expressionOut.print(message(code:'add'))
printHtmlPart(9)
invokeTag('message','g',24,['code':("sender.form.apiKey")],-1)
printHtmlPart(4)
invokeTag('textField','g',27,['name':("apiKey"),'value':(apiKey)],-1)
printHtmlPart(10)
invokeTag('message','g',35,['code':("sender.form.collapseKey")],-1)
printHtmlPart(4)
invokeTag('textField','g',38,['name':("collapseKey"),'value':(collapseKey)],-1)
printHtmlPart(11)
invokeTag('message','g',46,['code':("sender.form.message")],-1)
printHtmlPart(12)
loop:{
int index = 0
for( key in ([messageKey].flatten()) ) {
printHtmlPart(13)
invokeTag('message','g',53,['code':("sender.form.messageKey")],-1)
printHtmlPart(14)
invokeTag('textField','g',56,['name':("messageKey"),'value':(key)],-1)
printHtmlPart(15)
invokeTag('message','g',61,['code':("sender.form.messageValue")],-1)
printHtmlPart(14)
invokeTag('textField','g',64,['name':("messageValue"),'value':([messageValue].flatten()[index])],-1)
printHtmlPart(16)
index++
}
}
printHtmlPart(17)
expressionOut.print(resource(dir:'images/skin',file:'add.png'))
printHtmlPart(18)
expressionOut.print(message(code:'add'))
printHtmlPart(19)
invokeTag('submitButton','g',80,['name':("send"),'value':(message(code:'send'))],-1)
printHtmlPart(20)
})
invokeTag('form','g',83,['name':("message"),'action':("sendMessage")],1)
printHtmlPart(21)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1408370172000L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'html'
public static final String TAGLIB_CODEC = 'none'
}
