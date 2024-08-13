package com.example.wellbook_drawer.ui.aggiungiPrenotazione

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wellbook_drawer.Database.Database_wellApp
import com.example.wellbook_drawer.Database.ReservationDB.Reservation
import com.example.wellbook_drawer.Database.UserDB.User
import com.example.wellbook_drawer.R
import com.example.wellbook_drawer.databinding.FragmentAggiungiprenotazioneBinding
import com.example.wellbook_drawer.ui.aggiungiUtente.AddUser_ViewModel
import com.example.wellbook_drawer.ui.aggiungiUtente.UserVMFactory
import java.text.SimpleDateFormat
import java.util.Locale

class AddReservation_Fragment : Fragment() {
    // binding
    private var _binding: FragmentAggiungiprenotazioneBinding? = null
    private lateinit var etDate: EditText
    private lateinit var etStartTime: EditText
    private lateinit var etFinishTime: EditText
    private var selectedUser: User? = null


    private val binding get() = _binding!!
    private var viewModel: AddReservation_ViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val dao = Database_wellApp.getInstance(requireContext()).reservationDAO()
        val factory = ReservationVMFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(AddReservation_ViewModel::class.java)


        _binding = FragmentAggiungiprenotazioneBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUserSpinner()


        // ADD DATE PICKER
        etDate = binding.etDay
        etDate.setOnClickListener {
            showDatePickerDialog(etDate)
        }

        // ADD TIME PICKER
        etStartTime = binding.etStartTime
        etStartTime.setOnClickListener {
            showTimePickerDialog(etStartTime)
        }

        etFinishTime = binding.etFinishTime
        etFinishTime.setOnClickListener {
            showTimePickerDialog(etFinishTime)
        }

        binding.apply {
            btnSend.setOnClickListener {
                saveReservation()

            }
        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun showDatePickerDialog(etDate: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val dateFormat = SimpleDateFormat("EEEE d MMMM", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                etDate.setText(formattedDate)
            }, year, month, day)
        datePickerDialog.show()
    }

    fun showTimePickerDialog(etTime: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog =
            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                etTime.setText(selectedTime)
            }, hour, minute, true)
        timePickerDialog.show()
    }


    private fun setupUserSpinner() {
        viewModel?.getAllUsers()?.observe(viewLifecycleOwner) { users ->
            val userNames = users.map { it.name + " " + it.lastName }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.userSpinner.adapter = adapter

            binding.userSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>, view: View?, position: Int, id: Long
                    ) {
                        // Seleziona l'utente in base alla posizione selezionata
                        selectedUser = users[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Toast.makeText(requireContext(), "Seleziona un utente", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }
                }
        }
    }


    private fun saveReservation() {
        binding.apply {
            val endTime = etFinishTime.text.toString()
            val startTime = etStartTime.text.toString()
            val day = etDay.text.toString()

            val reservation = Reservation(
                0, selectedUser?.id ?: 0, startTime, endTime, day
            )
            if (endTime == startTime) {
                Toast.makeText(
                    requireContext(),
                    "L'orario di inizio e di fine devono essere diversi",
                    Toast.LENGTH_SHORT
                ).show()
                return
            } else viewModel?.addReservation(reservation)

            etDay.text.clear()
            userSpinner.setSelection(0)
            etStartTime.text.clear()
            etFinishTime.text.clear()
            val navController = findNavController()
            navController.navigate(R.id.view_Reservations)
        }
    }

}