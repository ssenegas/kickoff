package com.senegas.kickoff.pitches;

import com.senegas.kickoff.KickOff;
import com.senegas.kickoff.pitches.Pitch.Type;

public class PitchFactory {

    private static PitchFactory INSTANCE = new PitchFactory();

	private PitchFactory() {
	}

	public static PitchFactory getInstance() {
        return INSTANCE;
    }

	/**
	 * Make a pitch
	 * @param pitchType
	 * @return a pitch
	 */
	public Pitch make(KickOff app, Type pitchType)
	{
		switch( pitchType )
		{
			case CLASSIC:
				return new ClassicPitch(app);
			case WET:
				return new WetPitch(app);
			case SOGGY:
				return new SoggyPitch(app);
			case ARTIFICIAL:
				return new ArtificialPitch(app);
			case PLAYERMANAGER:
				return new PlayerManagerPitch(app);
			default:
				return new ClassicPitch(app);
	    }
	}
}
