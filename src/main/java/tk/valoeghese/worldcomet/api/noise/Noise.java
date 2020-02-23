package tk.valoeghese.worldcomet.api.noise;

public interface Noise {
	double sample(double x, double y);
	double sample(double x, double y, double z);
}
