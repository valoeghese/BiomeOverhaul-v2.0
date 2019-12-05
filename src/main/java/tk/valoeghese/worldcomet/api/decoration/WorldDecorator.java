package tk.valoeghese.worldcomet.api.decoration;

import java.util.ArrayList;
import java.util.List;

public class WorldDecorator {
	public final Iterable<Decorator> decorators;

	private WorldDecorator(Builder builder) {
		this.decorators = builder.decorators;
	}

	public static class Builder {
		private final List<Decorator> decorators = new ArrayList<>();

		private Builder() {
		}

		public Builder addDecorator(Decorator decorator) {
			this.decorators.add(decorator);
			return this;
		}

		public WorldDecorator build() {
			return new WorldDecorator(this);
		}
	}
}
