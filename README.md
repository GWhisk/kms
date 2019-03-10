
<img src="https://raw.githubusercontent.com/GWhisk/kms/master/kms_logo.png" width="200" height="75">

A command line program that helps you make the ultimate decision ... _cleanly_

##
**BEFORE YOU CONTINUE, PLEASE READ THE [WIKI](https://github.com/GWhisk/kms/wiki/Actual-Purpose-of-kms).**

kms is a **protest piece / protest software.** The program itself isn't meant to be taken **literally**. 

Read more about it here: [**LINK**](https://github.com/GWhisk/kms/wiki/Actual-Purpose-of-kms).
##
# Purpose


There comes a moment, if not multiple moments, in our lives where we question every aspect of our existence. Sometimes, we get to a point where we ask ourselves if continuing to live is even worth it at all.

Most of us have all dealt, and continue to deal with:

* Loss, or death
* Heartbreak
* Immense dissapointment
* Growing self-hate
* Feeling like an impostor, or a fraud
* Being an immense dissapointment
* Crippling stress
* Crippling loneliness
* Social exclusion
* Illness (be it a loved one or yourself)
* ...and many other situations

And sometimes, we don't have the tools, resources or people to help us work through these difficult times. 

And due to that, sometimes, we don't get through it at all and let everything build up inside us. Our reason begins to fade, and along with it, our will to continue seeing life through.

Suicide becomes the attractive option. The only option.

## <font face="courier new"  color="black">kms</font> usage
<font face="courier new" color="black">kms</font> allows one to set a list of commands to be executed at set point in time (PIT),

A PIT can be entered as a concatenation of date and time (in 24 hour format), as such:  <font face="courier new" color="black"> YYYY:mm:dd:hh:mm </font>

* To represent March 14, 2019 on time 21:30, the PIT would be: <font face="courier new" color="black"> 2019:03:14:21:30 </font>

A PIT can also be entered as just the time during the current day, as such: <font face="courier new" color="black"> hh:mm </font>
* To represent 19:30 (or 7:30 pm) , the PIT would be: <font face="courier new" color="black"> 19:30 </font>

Once a PIT has been determined, <font face="courier new" color="black">kms</font> can be used as such:`kms PIT {command1},{command2}, ... `


## <font face="courier new"  color="black">kms</font> commands

Usage of <font face="courier new"  color="black">kms</font> commands is as follows: `COMMAND_NAME <ARG1> <ARG2> ... `

Currently, <font face="courier new"  color="black">kms</font> supports 5 standard commands:

*  <font face="courier new"  color="black">delete </font>- deletes a list of folders and/or files
	* Accepts at least one arguments: all of which are expected to be valid file or directory paths.
	* Example: `delete <story.txt> <stories/> <music.mp3>`
		* This deletes the file <font face="courier new"  color="black">story.txt</font> , the files inside <font face="courier new"  color="black">stories/</font> and its sub-directories, and <font face="courier new"  color="black">music.mp3</font>
* <font face="courier new"  color="black">make</font> - creates a new file with a given path and allows the user to write a textual message in it
	* Accepts two arguments: The first being the path of the file to write to/or create (if yet to exist). The second being the message to write.
	* Example: `make <farewell.txt> <I'm sorry>`
		* First checks whether <font face="courier new"  color="black">farewell.txt</font> exists, if not, it creates the file. Then, it write `I'm sorry` to the file
* <font face="courier new"  color="black">display</font> - displays an image in full screen view for a set amount of seconds, or indefinitely. (JPEG, PNG and GIF images are supported)
	* Accepts two arguments: The first being the path of the image. The second being the amount of seconds to display the image for.
	* Example: `display <farewell.jpg> <10>`
		* Displays the JPG <font face="courier new"  color="black">farewell.jpg</font> for 10 seconds
* <font face="courier new"  color="black">play</font> - plays an audio file (MP3, AAC, PCM) to completion, or for a set amount of seconds
	* Accepts two arguments: The first being the path to the audio file. The second being the duration, in seconds, to play the audio file for - or a negative value to play the file to completion.
	* Example: `play <no fun.mp3> <10>`
		* Plays the MP3 file `no fun.mp3` for 10 seconds
* <font face="courier new"  color="black">exec</font> - executes shell commands
	* Accepts one argument: The whole shell command to execute
	* Example: `exec <rm -rf .git>`
		* Executes the shell command, and it's options and arguments `rm -rf .git`

## Example use of <font face="courier new"  color="black">kms</font>

 **Scenario:**  It's 11 p.m and you're typing rapidly into Vim. You think to yourself: _"If I just switch these two variables around, my program should work."_ . You enter `:w` in hopes of finally fixing that damned segfault error that has been haunting you for the past four hours. The compiler is silent, but finishes nonetheless. This time, the program runs for a little longer. No errors are being printed out, no weird characters - everything is quiet on the digital front. 

<font face="courier new"  color="red"> **Segmentation fault (core dumped)** </font>

Defeated, the clock stares you down, mocking you as it ticks your time away. You take another look at your screen, trying to sort through the spaghetti code your hands typed out. The black void that surrounds each character seemingly grows with each stare. Staring starts to hurt, so you look around the room to ease your eyes.

It's barren. No one else is here. Your classmates have finished. The tutors have gone home. The TA's are probably drinking themselves to amnesia. There is no help. There is no hope. 

So, you start to calculate your chances of passing this class. Your past assignment scores aren't stellar. You just bombed your midterm despite taking time out of your other classes for this course. The weekly quizzes are the only thing keeping you afloat. But the ice beneath you is shrinking. And you know you'll fail if you don't finish this assignment.

Your other classes aren't looking so good either. You had to half-ass your art history essay just so you can go to office hours for this class. And doing that only gave you non-answers from the person you would think would have solutions.  

And just before you started, you just received your math midterm scores. The median score looks good, and you started to feel hopeful. The class minimum is depressing, but at least it's not you right? Oh ... it is.

_ding_ . You just got an email. It's from your college counselor reminding you to stay above a 2.0 for the quarter or be put in jeopardy of being kicked out of the university. Fuck, you can't have that that. What would your parents think? They put all their hope, support and resources to get you through. But you realize that maybe, you can't get through. Your mind was never fit for this school. Just look at this room: you're the only one left. Everybody's done. You can't even fix a single bug. And if you can't do that, then what can you do? Build software for a Fortune 500 company? Hell no. You're not John, who was able to score his first internship at Facebook as a freshman. A freshman! John was able to finish in time also. He's always with his study group, cracking shitty programming jokes. You wish you had that: a group to belong to. Looking at it now, you really don't have anyone on campus to call as a friend. Perhaps the school psychologist? But you only see him every few weeks right? Kinda shitty for the school to not fund that kind of service more. That's life though....

Shame, guilt, blame, humiliation, embarrassment, desperation, anxiety, loneliness, depression ... You're mind is now on fire as your run through solutions. None of them work. 

_Except one._ you think to yourself.

You start looking up ways to do it. The goal is for a painless and quick method. You don't want anyone to interrupt you - which is unlikely looking at it now. Your card does allow for roof access so that's an option. There's also a pharmacy nearby if medication is needed.

Ah, good choice. Very creative. 

But you should leave a note. Things like this always have a note. Didn't you have you make that Java program that can submit posts to your Facebook account? Maybe you can finally put it to good use.

`kms 12:00 {exec javac -jar fbposter.java last_message.txt}` , you enter into the terminal. 

You drive out to the nearby pharmacy and purchase the tools, along with a bottle of vodka. It's a bit pricey, but you have to live life to your fullest right? 

The lab is still empty. You begin chugging down the vodka, hoping to feel that buzz and warmth. Instead you puke. Questions of doubt begin to raise through your mind, but it's too late, the alcohol has reached your mind and washed it of uncertainty. 

The numbing cream was a good choice. It makes the cutting less painful. You rub it on your wrists and can immediately feel the effects. The razors are a bit old fashioned, but it's not like the pharmacy was selling knives. Remember: across the line for punishment, along it for release. 

For a moment, your senses return. 
For a moment, your doubts are casted aside by an array of hope. 
For a moment, you reason with yourself of other options other than this. 
For a moment, you consider just going home and planning for a new future.

Then you realize: it's all going to be the same. The truth is, you've always been at the bottom of the abyss. For every small achievement you feel, a greater failure awaits you. No achievement can climb you out of the hole you dug yourself in. You've been clawing your way out for way too long. You're tired. You've given up. There's nothing else you can do. Other than ... _leave_.

The cream made the cutting quick and relative painless. But it's not like you were aware of it, or did a good job of it, with much of your mind buzzed out. 

Damn, that's a lot of blood. Jesus, the keyboard is drenched with it. You decide to just let your arms hang by your sides. It makes the draining easier.

A feeling of physical weakness takes over your body. Your face starts to feel cold and it becomes harder to move your arms. Your thoughts loose cohesion as you start to black out. It's getting difficult to keep your eyes open. Is this how it feels to finally sleep? 

It's 12:00 a.m. Your screen shows:

`12:00 trigger activated. Commands executed.`

But you wouldn't know that.




