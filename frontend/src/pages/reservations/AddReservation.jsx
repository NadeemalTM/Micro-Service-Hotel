import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { useMutation, useQuery } from '@tanstack/react-query';
import { createReservation, getAvailableRoomsByType } from '../../services/api';

const AddReservation = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    guestName: '',
    guestEmail: '',
    guestPhone: '',
    guestAddress: '',
    identificationType: 'PASSPORT',
    identificationNumber: '',
    checkInDate: '',
    checkOutDate: '',
    numberOfGuests: 1,
    numberOfAdults: 1,
    numberOfChildren: 0,
    roomType: 'STANDARD',
    specialRequests: '',
    breakfastIncluded: true,
    parkingRequired: false,
    airportPickup: false
  });

  const { data: availableRooms } = useQuery({
    queryKey: ['available-rooms', formData.roomType],
    queryFn: async () => {
      const response = await getAvailableRoomsByType(formData.roomType);
      return response.data.data;
    },
    enabled: !!formData.roomType
  });

  const mutation = useMutation({
    mutationFn: createReservation,
    onSuccess: () => {
      alert('Reservation created successfully!');
      navigate('/reservations');
    },
    onError: (error) => {
      alert('Failed to create reservation: ' + (error.response?.data?.message || error.message));
    }
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    const selectedRoom = availableRooms?.[0];
    if (!selectedRoom) {
      alert('No available rooms of this type');
      return;
    }
    mutation.mutate({
      ...formData,
      roomId: selectedRoom.id,
      roomNumber: selectedRoom.roomNumber,
      pricePerNight: selectedRoom.pricePerNight,
      status: 'CONFIRMED',
      paymentStatus: 'PENDING',
      bookedBy: 'admin'
    });
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  return (
    <div>
      <Link to="/reservations" className="btn btn-secondary" style={{ marginBottom: '2rem', display: 'inline-flex' }}>
        <ArrowLeft size={20} /> Back
      </Link>
      <div className="card">
        <h2>Add New Reservation</h2>
        <form onSubmit={handleSubmit} style={{ marginTop: '2rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '1.5rem' }}>
            <div>
              <label>Guest Name *</label>
              <input type="text" name="guestName" value={formData.guestName} onChange={handleChange} required />
            </div>
            <div>
              <label>Email *</label>
              <input type="email" name="guestEmail" value={formData.guestEmail} onChange={handleChange} required />
            </div>
            <div>
              <label>Phone *</label>
              <input type="tel" name="guestPhone" value={formData.guestPhone} onChange={handleChange} placeholder="+94-77-1234567" required />
            </div>
            <div>
              <label>Address</label>
              <input type="text" name="guestAddress" value={formData.guestAddress} onChange={handleChange} />
            </div>
            <div>
              <label>ID Type *</label>
              <select name="identificationType" value={formData.identificationType} onChange={handleChange} required>
                <option value="PASSPORT">Passport</option>
                <option value="NATIONAL_ID">National ID</option>
                <option value="DRIVING_LICENSE">Driving License</option>
              </select>
            </div>
            <div>
              <label>ID Number *</label>
              <input type="text" name="identificationNumber" value={formData.identificationNumber} onChange={handleChange} required />
            </div>
            <div>
              <label>Check-in Date *</label>
              <input type="date" name="checkInDate" value={formData.checkInDate} onChange={handleChange} required />
            </div>
            <div>
              <label>Check-out Date *</label>
              <input type="date" name="checkOutDate" value={formData.checkOutDate} onChange={handleChange} required />
            </div>
            <div>
              <label>Room Type *</label>
              <select name="roomType" value={formData.roomType} onChange={handleChange} required>
                <option value="STANDARD">Standard</option>
                <option value="DELUXE">Deluxe</option>
                <option value="SUITE">Suite</option>
                <option value="EXECUTIVE">Executive</option>
                <option value="PRESIDENTIAL">Presidential</option>
              </select>
              {availableRooms && <small style={{color: 'green'}}>{availableRooms.length} rooms available</small>}
            </div>
            <div>
              <label>Number of Guests *</label>
              <input type="number" name="numberOfGuests" value={formData.numberOfGuests} onChange={handleChange} min="1" required />
            </div>
            <div>
              <label>Adults *</label>
              <input type="number" name="numberOfAdults" value={formData.numberOfAdults} onChange={handleChange} min="1" required />
            </div>
            <div>
              <label>Children</label>
              <input type="number" name="numberOfChildren" value={formData.numberOfChildren} onChange={handleChange} min="0" />
            </div>
          </div>
          <div style={{ marginTop: '1.5rem' }}>
            <label>Special Requests</label>
            <textarea name="specialRequests" value={formData.specialRequests} onChange={handleChange} rows="3" />
          </div>
          <div style={{ marginTop: '1.5rem', display: 'flex', gap: '2rem' }}>
            <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <input type="checkbox" name="breakfastIncluded" checked={formData.breakfastIncluded} onChange={handleChange} />
              Breakfast Included
            </label>
            <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <input type="checkbox" name="parkingRequired" checked={formData.parkingRequired} onChange={handleChange} />
              Parking Required
            </label>
            <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <input type="checkbox" name="airportPickup" checked={formData.airportPickup} onChange={handleChange} />
              Airport Pickup
            </label>
          </div>
          <div style={{ marginTop: '2rem', display: 'flex', gap: '1rem' }}>
            <button type="submit" className="btn btn-primary" disabled={mutation.isPending}>
              {mutation.isPending ? 'Creating...' : 'Create Reservation'}
            </button>
            <Link to="/reservations" className="btn btn-secondary">Cancel</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddReservation;
