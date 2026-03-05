import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { useMutation } from '@tanstack/react-query';
import { createEvent } from '../../services/api';

const AddEvent = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    eventCode: '',
    eventName: '',
    eventType: 'CONFERENCE',
    venue: 'GRAND_BALLROOM',
    startDate: '',
    endDate: '',
    startTime: '',
    endTime: '',
    expectedAttendees: 0,
    contactPerson: '',
    contactEmail: '',
    contactPhone: '',
    cateringRequired: true,
    cateringDetails: '',
    equipmentNeeded: '',
    decorationRequirements: '',
    estimatedCost: 0,
    advancePayment: 0,
    notes: ''
  });

  const mutation = useMutation({
    mutationFn: createEvent,
    onSuccess: () => {
      alert('Event created successfully!');
      navigate('/events');
    },
    onError: (error) => {
      alert('Failed to create event: ' + (error.response?.data?.message || error.message));
    }
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    mutation.mutate({
      ...formData,
      status: 'PLANNED',
      balanceAmount: formData.estimatedCost - formData.advancePayment
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
      <Link to="/events" className="btn btn-secondary" style={{ marginBottom: '2rem', display: 'inline-flex' }}>
        <ArrowLeft size={20} /> Back
      </Link>
      <div className="card">
        <h2>Create New Event</h2>
        <form onSubmit={handleSubmit} style={{ marginTop: '2rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '1.5rem' }}>
            <div>
              <label>Event Code *</label>
              <input type="text" name="eventCode" value={formData.eventCode} onChange={handleChange} placeholder="e.g., EVT-007" required />
            </div>
            <div>
              <label>Event Name *</label>
              <input type="text" name="eventName" value={formData.eventName} onChange={handleChange} required />
            </div>
            <div>
              <label>Event Type *</label>
              <select name="eventType" value={formData.eventType} onChange={handleChange} required>
                <option value="WEDDING">Wedding</option>
                <option value="CONFERENCE">Conference</option>
                <option value="PARTY">Party</option>
                <option value="MEETING">Meeting</option>
                <option value="SEMINAR">Seminar</option>
                <option value="EXHIBITION">Exhibition</option>
                <option value="OTHER">Other</option>
              </select>
            </div>
            <div>
              <label>Venue *</label>
              <select name="venue" value={formData.venue} onChange={handleChange} required>
                <option value="GRAND_BALLROOM">Grand Ballroom</option>
                <option value="CONFERENCE_ROOM_A">Conference Room A</option>
                <option value="CONFERENCE_ROOM_B">Conference Room B</option>
                <option value="GARDEN">Garden</option>
                <option value="POOLSIDE">Poolside</option>
                <option value="ROOFTOP">Rooftop</option>
              </select>
            </div>
            <div>
              <label>Start Date *</label>
              <input type="date" name="startDate" value={formData.startDate} onChange={handleChange} required />
            </div>
            <div>
              <label>End Date *</label>
              <input type="date" name="endDate" value={formData.endDate} onChange={handleChange} required />
            </div>
            <div>
              <label>Start Time *</label>
              <input type="time" name="startTime" value={formData.startTime} onChange={handleChange} required />
            </div>
            <div>
              <label>End Time *</label>
              <input type="time" name="endTime" value={formData.endTime} onChange={handleChange} required />
            </div>
            <div>
              <label>Expected Attendees *</label>
              <input type="number" name="expectedAttendees" value={formData.expectedAttendees} onChange={handleChange} min="1" required />
            </div>
            <div>
              <label>Contact Person *</label>
              <input type="text" name="contactPerson" value={formData.contactPerson} onChange={handleChange} required />
            </div>
            <div>
              <label>Contact Email *</label>
              <input type="email" name="contactEmail" value={formData.contactEmail} onChange={handleChange} required />
            </div>
            <div>
              <label>Contact Phone *</label>
              <input type="tel" name="contactPhone" value={formData.contactPhone} onChange={handleChange} placeholder="+94-77-1234567" required />
            </div>
            <div>
              <label>Estimated Cost (LKR) *</label>
              <input type="number" name="estimatedCost" value={formData.estimatedCost} onChange={handleChange} step="0.01" min="0" required />
            </div>
            <div>
              <label>Advance Payment (LKR)</label>
              <input type="number" name="advancePayment" value={formData.advancePayment} onChange={handleChange} step="0.01" min="0" />
            </div>
          </div>
          
          <div style={{ marginTop: '1.5rem' }}>
            <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <input type="checkbox" name="cateringRequired" checked={formData.cateringRequired} onChange={handleChange} />
              Catering Required
            </label>
          </div>
          
          {formData.cateringRequired && (
            <div style={{ marginTop: '1rem' }}>
              <label>Catering Details</label>
              <textarea name="cateringDetails" value={formData.cateringDetails} onChange={handleChange} rows="2" placeholder="e.g., Traditional Sri Lankan buffet for 200 guests" />
            </div>
          )}
          
          <div style={{ marginTop: '1rem' }}>
            <label>Equipment Needed</label>
            <textarea name="equipmentNeeded" value={formData.equipmentNeeded} onChange={handleChange} rows="2" placeholder="e.g., Projector, sound system, podium" />
          </div>
          
          <div style={{ marginTop: '1rem' }}>
            <label>Decoration Requirements</label>
            <textarea name="decorationRequirements" value={formData.decorationRequirements} onChange={handleChange} rows="2" placeholder="e.g., Traditional floral decorations, lighting" />
          </div>
          
          <div style={{ marginTop: '1rem' }}>
            <label>Additional Notes</label>
            <textarea name="notes" value={formData.notes} onChange={handleChange} rows="2" />
          </div>
          
          <div style={{ marginTop: '1.5rem', padding: '1rem', background: '#f0f9ff', borderRadius: '8px' }}>
            <p><strong>Estimated Cost: LKR {parseFloat(formData.estimatedCost || 0).toLocaleString()}</strong></p>
            <p><strong>Advance Payment: LKR {parseFloat(formData.advancePayment || 0).toLocaleString()}</strong></p>
            <p><strong>Balance: LKR {(parseFloat(formData.estimatedCost || 0) - parseFloat(formData.advancePayment || 0)).toLocaleString()}</strong></p>
          </div>
          
          <div style={{ marginTop: '2rem', display: 'flex', gap: '1rem' }}>
            <button type="submit" className="btn btn-primary" disabled={mutation.isPending}>
              {mutation.isPending ? 'Creating...' : 'Create Event'}
            </button>
            <Link to="/events" className="btn btn-secondary">Cancel</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddEvent;
