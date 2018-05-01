package com.senegas.kickoff.pitches;

import com.senegas.kickoff.pitches.Pitch.Type;

public class PitchFactory {
	/**
	 * Make a pitch
	 * @param pitchType
	 * @return a pitch
	 */
	public static Pitch make(Type pitchType)
	{ 
		switch( pitchType )
		{
	        case CLASSIC:        return new ClassicPitch();
	        case WET:            return new WetPitch();
	        case SOGGY:          return new SoggyPitch();
	        case ARTIFICIAL:     return new ArtificialPitch();
	        case PLAYERMANAGER:  return new PlayerManagerPitch();
	        default:             return new ClassicPitch();
	    }
	}
}
