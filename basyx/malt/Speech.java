package eclipse.basyx.malt;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.*;
import java.nio.*;
import java.util.List;


/////////////////////TO DO ////////////////////////////
public class Speech {
	public static void main(String... args) throws Exception {
		
		
		
		//start a client
		try (SpeechClient speechClient = SpeechClient.create()) {

		      // The path to the audio file to transcribe
		      String gcsUri = "gs://cloud-samples-data/speech/brooklyn_bridge.raw";

		      // Builds the sync recognize request
		      RecognitionConfig config =
		          RecognitionConfig.newBuilder()
		              .setEncoding(AudioEncoding.LINEAR16)
		              .setSampleRateHertz(16000)
		              .setLanguageCode("en-US")
		              .build();
		      RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();

		      //Performing speech recognition on audio file
		      RecognizeResponse res = speechClient.recognize(config,audio);
		      List<SpeechRecognitionResult> results = res.getResultsList();
		      
		      
		      
		      for (SpeechRecognitionResult result : results) {
		    	  SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
		    	  System.out.printf("Transcription: %s%n",alternative.getTranscript());
		      }
	}
	
}

}
