import { useLocation } from "react-router-dom";
import "./MultiPlayerCode.css";

const MultiPlayerCode = () => {

    const location = useLocation();
    const { gameCode } = location.state || {};

    return (
        <div className="code">
            <h2>Your game code is generated!</h2>
            {gameCode ? (
                <p>Share this code with your friend to join: <strong>{gameCode}</strong></p>
            ) : (
                <p>Error: Game code not found.</p>
            )}
        </div>
    );
}

export default MultiPlayerCode;