import os
import re
import webapp2
import jinja2
import time
import hashlib
import hmac

from string import letters
from secret import *

from google.appengine.ext import db

template_dir = os.path.join(os.path.dirname(__file__), 'templates')
jinja_env = jinja2.Environment(loader = jinja2.FileSystemLoader(template_dir),
                                                                autoescape = True)
###################HASHING FUNCTIONS##################################
def hash_str(s):
    return hmac.new(secret, s).hexdigest()

def make_secure_val(s):
    return "%s|%s" % (s, hash_str(s))

def check_secure_val(h):
    val = h.split('|')[0]
    if h == make_secure_val(val):
        return val

class Handler(webapp2.RequestHandler):
    def write(self, *a, **kw):
        self.response.out.write(*a, **kw)

    def render_str(self, template, **params):
        t = jinja_env.get_template(template)
        return t.render(params)

    def render(self, template, **kw):
        self.write(self.render_str(template, **kw))

    def set_cookie(self, name, val):
        cookie_val = make_secure_val(val)
        self.response.headers.add_header('Set-Cookie', '%s=%s' % (name, cookie_val))

    def read_cookie(self, name, val):
        cookie_val = self.request.cookies.get(name)
        if cookie_val and check_secure_val(cookie_val):
            return cookie_val

class Blog(db.Model):
    #user = db.StringProperty(required = True)
    #password = db.StringProperty(required = True)
    subject = db.StringProperty(required = True)
    blog = db.TextProperty(required = True)
    created = db.DateTimeProperty(auto_now_add = True)
    last_modified = db.DateTimeProperty(auto_now = True)

class IndexPage(Handler):
    def get(self):
        self.render("index.html")

class MainPage(Handler):

    def render_front(self, subject="", blog="", error=""):
        blogs = db.GqlQuery("SELECT * FROM Blog "
                            "ORDER BY created DESC ")
        time.sleep(1)
        self.render("front.html", subject= subject, blog= blog, error = error, blogs = blogs)

    def get(self):
        self.render_front("front.html")


class FormPage(Handler):

    def get(self):
        self.render("form.html")

    def post(self):
        subject = self.request.get("subject")
        blog = self.request.get("blog")

        if subject and blog:
            b = Blog(subject = subject, blog = blog)
            b_key = b.put()

            self.redirect("/blog/%d" % b_key.id())
        else:
            error = "we need both a subject and a body"
            self.render(subject, blog, error)


#Signup to work on. For hashing and validity checks
#Welcome page of user using cookie
#Add projects to website and uploading!
#Changes to CSS of template to make it not seem like a complete copy paste.

class Permalink(MainPage):
    def get(self, blog_id):
        s = Blog.get_by_id(int(blog_id))
        self.render("front.html", blogs=[s])


class SignUp(Handler):
    def get(self):
        visits = 0
        visit_cookie_str  = self.request.cookies.get('visits')
        if visit_cookie_str:
            cookie_val = check_secure_val(visit_cookie_str)
            if cookie_val:
                visits = int(cookie_val)
        visits += 1
        new_cookie_val = make_secure_val(str(visits))
        self.response.headers.add_header('Set-Cookie', 'visits=%s' % new_cookie_val)
        self.write("You've been here %s times!\n" % visits)
        self.render("signup.html")

    def post(self):
        username = self.request.get("username")
        password = self.request.get("password")
        ver_password = self.request.get("ver_password")
        email = self.request.get("email")

        if username and password and ver_password and (password == ver_password):
            self.redirect("/welcome")

class LogIn(Handler):
    def get(self):
        self.render("login.html")

    def post(self):
        username = self.request.get("username")
        password = self.request.get("password")
        if check_secure_val(make_secure_val(password)):
            self.redirect('/welcome')

app = webapp2.WSGIApplication([ ('/', IndexPage),
                                ('/blog', MainPage),
                                ('/newpost', FormPage),
                                ('/blog/(\d+)', Permalink),
                                ('/signup', SignUp),
                                ('/login', LogIn)
                               ],
                                debug = True)
