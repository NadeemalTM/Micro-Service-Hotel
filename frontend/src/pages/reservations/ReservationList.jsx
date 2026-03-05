import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { Plus, Eye, Calendar, Search } from 'lucide-react';
import { getAllReservations } from '../../services/api';
import { useState } from 'react';

const ReservationList = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  
  const { data: reservations, isLoading } = useQuery({
    queryKey: ['reservations'],
    queryFn: async () => {
      const response = await getAllReservations();
      return response.data.data;
    },
  });

  const getStatusBadge = (status) => {
    const colors = {
      CONFIRMED: 'background: #dbeafe; color: #1e40af',
      CHECKED_IN: 'background: #d1fae5; color: #065f46',
      CHECKED_OUT: 'background: #e5e7eb; color: #374151',
      CANCELLED: 'background: #fee2e2; color: #991b1b',
      PENDING: 'background: #fef3c7; color: #92400e'
    };
    return (
      <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', fontWeight: '500', ...colors[status] }}>
        {status}
      </span>
    );
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', { 
      year: 'numeric', 
      month: 'short', 
      day: 'numeric' 
    });
  };

  // Filter reservations based on search and status
  const filteredReservations = reservations?.filter(reservation => {
    const matchesSearch = reservation.guestName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         reservation.guestEmail?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         reservation.roomNumber?.toString().includes(searchTerm);
    const matchesStatus = statusFilter === 'ALL' || reservation.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  if (isLoading) return <div className="spinner"></div>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Reservations</h1>
        <Link to="/reservations/add" className="btn btn-primary">
          <Plus size={20} /> New Reservation
        </Link>
      </div>

      {/* Search and Filter Bar */}
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '1.5rem' }}>
        <div className="search-box" style={{ flex: 1 }}>
          <Search size={20} color="#6b7280" />
          <input
            type="text"
            placeholder="Search by guest name, email, or room number..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          style={{ padding: '0.75rem', borderRadius: '8px', border: '1px solid #d1d5db', minWidth: '150px' }}
        >
          <option value="ALL">All Status</option>
          <option value="CONFIRMED">Confirmed</option>
          <option value="CHECKED_IN">Checked In</option>
          <option value="CHECKED_OUT">Checked Out</option>
          <option value="PENDING">Pending</option>
          <option value="CANCELLED">Cancelled</option>
        </select>
      </div>

      {filteredReservations && filteredReservations.length > 0 ? (
        <div className="card">
          <div style={{ marginBottom: '1rem', color: '#6b7280', fontSize: '0.875rem' }}>
            Showing {filteredReservations.length} of {reservations.length} reservations
          </div>
          <div style={{ overflowX: 'auto' }}>
            <table>
              <thead>
                <tr>
                  <th>Guest Name</th>
                  <th>Room</th>
                  <th>Check-In</th>
                  <th>Check-Out</th>
                  <th>Guests</th>
                  <th>Price/Night (LKR)</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredReservations.map((reservation) => (
                  <tr key={reservation.id}>
                    <td>
                      <strong>{reservation.guestName}</strong>
                      <br />
                      <small style={{ color: '#64748b' }}>{reservation.guestEmail}</small>
                    </td>
                    <td>{reservation.roomNumber || 'N/A'}</td>
                    <td>
                      <Calendar size={14} style={{ display: 'inline', marginRight: '4px' }} />
                      {formatDate(reservation.checkInDate)}
                    </td>
                    <td>
                      <Calendar size={14} style={{ display: 'inline', marginRight: '4px' }} />
                      {formatDate(reservation.checkOutDate)}
                    </td>
                    <td>{reservation.numberOfGuests || (reservation.numberOfAdults || 0) + (reservation.numberOfChildren || 0)}</td>
                    <td>{parseFloat(reservation.pricePerNight || 0).toLocaleString()}</td>
                    <td>{getStatusBadge(reservation.status)}</td>
                    <td>
                      <button className="btn btn-secondary" style={{ padding: '0.5rem', fontSize: '0.875rem' }}>
                        <Eye size={16} /> View
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="card">
          <p style={{ textAlign: 'center', color: '#64748b' }}>
            {searchTerm || statusFilter !== 'ALL' 
              ? 'No reservations match your search criteria.' 
              : 'No reservations found. Create your first reservation!'}
          </p>
        </div>
      )}
    </div>
  );
};

export default ReservationList;
