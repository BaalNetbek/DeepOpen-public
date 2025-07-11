package AE;

public final class Time {
	private static String seconds;
	private static String minutes;
	private static String hours;

	private Time() {
	}

	public static String timeToHMS(final long var0) {
		final int var2 = (int)(var0 / 1000L % 60L);
		final int var3 = (int)(var0 / 60000L % 60L);
		int var4 = (int)(var0 / 3600000L % 24L);
		seconds = (var2 < 10 ? "0" : "") + var2;
		minutes = (var3 < 10 ? "0" : "") + var3;
		if (var4 == 0) {
			return (minutes + ":" + seconds);
		}
		final int var5 = (int)(var0 / 3600000L / 24L);
		var4 += var5 * 24;
		hours = (var4 < 10 ? "0" : "") + var4;
		return (hours + ":" + minutes + ":" + seconds);
	}

	public static String timeToHM(final long var0) {
		final int var2 = (int)(var0 / 60000L % 60L);
		int var3 = (int)(var0 / 3600000L % 24L);
		minutes = (var2 < 10 ? "0" : "") + var2;
		final int var4 = (int)(var0 / 3600000L / 24L);
		var3 += var4 * 24;
		hours = (var3 < 10 ? "0" : "") + var3;
		return (hours + ":" + minutes);
	}
}
