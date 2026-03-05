import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { createRoom } from '../../services/api';

const AddRoom = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    roomNumber: '', floor: '', roomType: 'STANDARD', status: 'AVAILABLE',
    pricePerNight: '', capacity: '', bedCount: '', bedType: 'QUEEN',
    hasBalcony: false, hasSeaView: false, roomSize: '',
    amenities: '', description: ''
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createRoom(formData);
      alert('Room created successfully!');
      navigate('/rooms');
    } catch (error) {
      alert('Error creating room');
    }
  };

  const handleChange = (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
    setFormData({ ...formData, [e.target.name]: value });
  };

  return (
    <div>
      <Link to="/rooms" className="btn btn-secondary" style={{ marginBottom: '2rem', display: 'inline-flex' }}>
        <ArrowLeft size={20} /> Back
      </Link>
      
      <div className="card">
        <h2>Add New Room</h2>
        <form onSubmit={handleSubmit} style={{ marginTop: '2rem' }}>
          <div className="grid grid-2">
            <div className="form-group">
              <label className="form-label">Room Number</label>
              <input className="form-input" name="roomNumber" value={formData.roomNumber} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Floor</label>
              <input type="number" className="form-input" name="floor" value={formData.floor} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Room Type</label>
              <select className="form-select" name="roomType" value={formData.roomType} onChange={handleChange}>
                <option value="STANDARD">Standard</option>
                <option value="DELUXE">Deluxe</option>
                <option value="SUITE">Suite</option>
                <option value="EXECUTIVE">Executive</option>
                <option value="PRESIDENTIAL">Presidential</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Price per Night</label>
              <input type="number" step="0.01" className="form-input" name="pricePerNight" value={formData.pricePerNight} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Capacity</label>
              <input type="number" className="form-input" name="capacity" value={formData.capacity} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Bed Count</label>
              <input type="number" className="form-input" name="bedCount" value={formData.bedCount} onChange={handleChange} required />
            </div>
          </div>
          <button type="submit" className="btn btn-primary">Create Room</button>
        </form>
      </div>
    </div>
  );
};

export default AddRoom;
