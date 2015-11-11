<%-- 
    Document   : topmenu_right
    Created on : Jun 2, 2014, 9:52:52 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="homebox">
    <div class="ui-widget">
        <h2>Login</h2>
        <form method="POST" action="/login">
            <div>
                <label for="username">Username:</label>
                <input type="text" name="username" autofocus="true"/>
            </div>
            <div>
                <label for="password">Password:</label>
                <input type="password" name="password" autocomplete="false"/>
            </div>
            <div>
                <label for="username"></label>
                <input type="submit" name="submit" value="Login"/>
            </div>
            <div>
                <label></label>
            </div>
            <div>
                <label></label>
            </div>
        </form>
    </div>
</div>

<div class="homebox">
    <div class="ui-widget">
        <h2>Register</h2>
        <form method="POST" action="/register">
            <div>
                <label for="username">Username:</label>
                <input type="text" name="username" autofocus="true"/>
            </div>
            <div>
                <label for="password">Password:</label>
                <input type="password" name="password" autocomplete="false"/>
            </div>
            <div>
                <label for="password2">confirm:</label>
                <input type="password" name="password2" autocomplete="false"/>
            </div>
            <div>
                <label for="password2">Email</label>
                <input type="text" name="email"/>
            </div>
            <div>
                <label for="submit"></label>
                <input type="submit" name="submit" value="Register"/>
            </div>
        </form>
    </div>
</div>

<div class="homebox">
    <div class="ui-widget">
        <h2>Login/Register via Social Media</h2>
        <p>You can also login or register by logging in with Social Media</p>
        <p>Note: You can add these to your account at any time</p>
        <a href="/login/twitter"><img src="/images/twitter/sign-in-with-twitter-gray.png" alt="Twitter"/></a>
    </div>
</div>

<h2 style="clear:both;">What does registration provide you?</h2>
<p>Right now not much, but future releases will provide additional services which will require registration. Currently on the list are:</p>
<ul>
    <li>Personalisation
        <ul>
            <li>you'll be able to monitor your frequent trains, see how they performed over time etc</li>
            <li>Store your most used stations, so you can go straight to them</li>
        </ul>
    </li>
    <li>Reporting
        <ul>
            <li>Ability to request custom reports, more useful for Rail User Groups</li>
        </ul>
    </li>
</ul>

<h3>Why is email required</h3>
<p>It isn't required, and you can leave it blank. You'll only need it if you ever forget your password or you want reports sent to you once they have been generated.</p>
<p>Note: If you don't set an email address and you do forget your password then you'll loose access, unless you have also linked another social account.</p>

<h3>Can I log in via Facebook, Twitter etc?</h3>
<p>Yes, and you can register that way as well. Registering via a social media (or Google) account will give you the same access.</p>

<h3>Can I link my accounts</h3>
<p>You cannot link separate accounts but once logged in you can add social media accounts to the same one. At any one time an account must have at least one of the following linked to it:</p>
<ul>
    <li>Password</li>
    <li>Twitter</li>
    <li>Facebook</li>
    <li>Google</li>
</ul>

<h3>When I link an account what of my account will you have access to?</h3>
<p>Nothing. All linking the account does is to allow you to use that network to log in to here. We cannot access anything of yours that's not public, nor can we post to that account.</p>

<h3>Do I need to register here and on DepartureBoards.mobi?</h3>
<p>No. You'll only need to register once either here or on <a href="//departureboards.mobi">departureboards.mobi</a> and you can use the same account on both sites.</p>
<p>In the end you'll be able to use the same account across all of the sites here, not just the rail ones.</p>