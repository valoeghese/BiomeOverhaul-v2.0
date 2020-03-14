package tk.valoeghese.worldcomet.api.terrain;

public final class GeneratorSettings {
	public final int seaLevel;
	public final boolean vanillaCarving;

	private GeneratorSettings(Builder builder) {
		this.seaLevel = builder.seaLevel;
		this.vanillaCarving = builder.vanillaCarving;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private int seaLevel = 63;
		private boolean vanillaCarving = false;

		private Builder() {
		}

		public Builder seaLevel(int seaLevel) {
			this.seaLevel = seaLevel;
			return this;
		}

		public Builder vanillaCarving(boolean vanillaCarving) {
			this.vanillaCarving = vanillaCarving;
			return this;
		}

		public GeneratorSettings build() {
			return new GeneratorSettings(this);
		}
	}
}
