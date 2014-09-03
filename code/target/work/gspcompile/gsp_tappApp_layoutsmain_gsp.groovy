import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_tappApp_layoutsmain_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/layouts/main.gsp" }
public Object run() {
Writer out = getOut()
Writer expressionOut = getExpressionOut()
registerSitemeshPreprocessMode()
printHtmlPart(0)
if(true && (request.xhr)) {
printHtmlPart(1)
invokeTag('render','g',3,['template':("/layouts/mainContent")],-1)
printHtmlPart(2)
}
else {
printHtmlPart(3)
createTagBody(2, {->
printHtmlPart(4)
createTagBody(3, {->
createTagBody(4, {->
invokeTag('layoutTitle','g',8,['default':("Grails")],-1)
})
invokeTag('captureTitle','sitemesh',8,[:],4)
})
invokeTag('wrapTitleTag','sitemesh',8,[:],3)
printHtmlPart(5)
expressionOut.print(resource(dir:'css',file:'main.css'))
printHtmlPart(6)
expressionOut.print(resource(dir:'images',file:'favicon.ico'))
printHtmlPart(7)
invokeTag('layoutHead','g',11,[:],-1)
printHtmlPart(4)
invokeTag('javascript','g',12,['library':("application")],-1)
printHtmlPart(8)
})
invokeTag('captureHead','sitemesh',16,[:],2)
printHtmlPart(9)
createTagBody(2, {->
printHtmlPart(10)
expressionOut.print(resource(dir:'images',file:'spinner.gif'))
printHtmlPart(11)
expressionOut.print(message(code:'spinner.alt',default:'Loading...'))
printHtmlPart(12)
expressionOut.print(resource(dir:'images',file:'logo.jpg'))
printHtmlPart(13)
invokeTag('render','g',24,['template':("/layouts/mainContent")],-1)
printHtmlPart(14)
})
invokeTag('captureBody','sitemesh',27,[:],2)
printHtmlPart(15)
}
printHtmlPart(2)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1408371660000L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'html'
public static final String TAGLIB_CODEC = 'none'
}
