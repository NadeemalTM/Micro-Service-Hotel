import { useParams, Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { ArrowLeft } from 'lucide-react';
import { getEmployeeById } from '../../services/api';

const EmployeeDetails = () => {
  const { id } = useParams();
  
  const { data, isLoading } = useQuery({
    queryKey: ['employee', id],
    queryFn: async () => {
      const response = await getEmployeeById(id);
      return response.data.data;
    },
  });

  if (isLoading) return <div className="spinner"></div>;

  return (
    <div>
      <Link to="/employees" className="btn btn-secondary" style={{ marginBottom: '2rem', display: 'inline-flex' }}>
        <ArrowLeft size={20} /> Back
      </Link>
      
      <div className="card">
        <h2>{data?.firstName} {data?.lastName}</h2>
        <div style={{ marginTop: '2rem' }}>
          <p><strong>Email:</strong> {data?.email}</p>
          <p><strong>Phone:</strong> {data?.phone}</p>
          <p><strong>Department:</strong> {data?.department}</p>
          <p><strong>Position:</strong> {data?.position}</p>
          <p><strong>Salary:</strong> LKR {parseFloat(data?.salary || 0).toLocaleString()}</p>
          <p><strong>Status:</strong> <span className="badge badge-success">{data?.status}</span></p>
        </div>
      </div>
    </div>
  );
};

export default EmployeeDetails;
