package hr.tvz.android.fitnessapp.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.tvz.android.fitnessapp.databinding.FragmentFeaturesBinding

class FeaturesFragment : Fragment() {

    private var _binding: FragmentFeaturesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeaturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val features = listOf(
            "🏋️ Create and manage custom workouts",
            "📋 Add exercises with sets, reps, and weight",
            "🗑️ Swipe to delete workouts/exercises",
            "✅ Mark exercises as completed during workout",
            "📖 Save completed workouts to history with details",
            "⏰ Daily reminder notifications",
            "🎵 Background music toggle",
            "📊 View workout logs with completed exercises",
            "📱 Rotates & adapts to screen orientation"
        )

        binding.textViewFeatures.text = features.joinToString("\n\n")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
