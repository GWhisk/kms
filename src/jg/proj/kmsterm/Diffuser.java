package jg.proj.kmsterm;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.net.URI;

public class Diffuser {

  public static void entrance(){
    //Separating kms prints with personal prints
    for(int i = 0; i < 4; i++){
      System.out.println(".");
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        continue;
      }
    }

    String potUserName = System.getProperty("user.name");

    String [] messages = {
        "Hey "+potUserName+". Is that your name?",
        "I know this looks really creepy, but I just wanna ask you to breathe right now.",
        "Our lives are often so stressful that we rarely reserve time to fill our lungs....",
        "The human brain is such an incredibly powerful organ. If we restrict it of fresh oxygen....",
        "... we start to cloud our thinking. Certain emotions take hold and we often end up ...",
        "easily frustrated and angered. And when our breathing continues to shorten, these thoughts grow",
        "And when they do, we sometimes think that everything in our lives is frustrating.",
        "We become unmonitvated. Our passions weaken. And we get depressed.",
        "We start comparing ourselves to others more and suddenly, we begin to live a life not by our own progress",
        "But the progress of others.",
        ".....",
        ".....",
        "You need to know though: this is your life",
        "Your growth as a person is ... yours",
        "When you start to compare your progress to that of others, you begin to loose...",
        "... everything that makes you ... you.",
        "When we start to bend to the standards of others, we begin to loose our own standards",
        ".....",
        "I'm not saying that you should completely ignore everyone else.",
        "But I am asking you to breathe...",
        "Embrace who you are.",
        "Embrace your struggles",
        "Embrace your scars",
        "Embrace your quirks",
        "Embrace your failures",
        "...In this moment, embrace yourself and think about your life so far.",
        "and remember to breathe! We want all eyes on deck, not just frustration and anger",
        ", but also happiness, joy, love and hope.",
        "Especially hope!",
        "........................",
        "........................",
        "........................",
        "........................",
    };

    for(String string : messages){
      matrixPrint(string, 75);
      sleep(500);
    }

    matrixPrint("Okay, you were probably hoping that I'd stop also. ", 75);
    sleep(500);
    matrixPrint("But before I do, let me try and get you some help...", 75);

    //attempt to open browser
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
      try {
        Desktop.getDesktop().browse(new URI("https://suicidepreventionlifeline.org/"));
      } catch (Exception e) {}
    }

    matrixPrint("1-800-273-8255  <--- National Suicide Prevention Lifeline", 75);
    sleep(250);
    matrixPrint("https://suicidepreventionlifeline.org/chat/   <---- Lifeline Chat", 75);
    sleep(250);
    matrixPrint("https://afsp.org/find-support/ive-lost-someone/find-a-support-group/   <---- Find a Support Group", 75);

    matrixPrint("......................................", 75);
  }

  /**
   * Will print the given message, but each character is printed
   * at a delay of a set amount of miliseconds
   * @param line - the message to print
   * @param milisecPause - the amount of miliseconds to pause for
   */
  private static void matrixPrint(String line, long milisecPause){
    for(char c : line.toCharArray()){
      System.out.print(c);
      sleep(milisecPause);
    }

    System.out.print(System.lineSeparator());
  }

  /**
   * Pauses the current thread for the given amount amount
   * of milliseconds.
   */
  private static void sleep(long milisecs){
    try {
      Thread.sleep(milisecs);
    } catch (InterruptedException e) {
      return;
    }
  }
}
