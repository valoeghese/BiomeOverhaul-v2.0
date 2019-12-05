package tk.valoeghese.worldcomet.api.terrain;

public final class GeneratorSettings {
	public final int seaLevel;
	
	private GeneratorSettings(Builder builder) {
		this.seaLevel = builder.seaLevel;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private int seaLevel;
		
		private Builder() {
		}
		
		public Builder seaLevel(int seaLevel) {
			this.seaLevel = seaLevel;
			return this;
		}
		
		public GeneratorSettings build() {
			return new GeneratorSettings(this);
		}
	}
}
