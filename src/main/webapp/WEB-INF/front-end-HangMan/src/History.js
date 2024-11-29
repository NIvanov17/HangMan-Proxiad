import { useLocation, useNavigate } from "react-router-dom";

const History = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { username } = location.state || {};

    return (
        <div className="history">
            <h2>History</h2>
            <p>{username}</p>
        </div>
    );
}

export default History;