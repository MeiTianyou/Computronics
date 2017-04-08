package pl.asie.computronics.audio.tts.core;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

import java.util.Map;

/**
 * @author Vexatos
 */
@IFMLLoadingPlugin.TransformerExclusions("pl.asie.computronics.audio.tts.core")
@MCVersion("1.7.10")
public class TextToSpeechCore extends DummyModContainer implements IFMLLoadingPlugin {

	public TextToSpeechCore() {
		super(makeMetadata());
	}

	private static ModMetadata makeMetadata() {
		ModMetadata metadata = new ModMetadata();
		metadata.autogenerated = false;
		metadata.authorList.add("Vexatos");
		metadata.credits = "Vexatos";
		metadata.modId = "Computronics-TTS";
		metadata.version = "1.0.0";
		metadata.name = "Computronics-Text-to-Speech";
		metadata.description = "Text-to-speech coremod for MaryTTS";
		return metadata;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return getClass().getName();
	}

	@Override
	public String getSetupClass() {
		return "pl.asie.computronics.audio.tts.core.TextToSpeechSetup";
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
