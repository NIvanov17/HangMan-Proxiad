import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./JoinGame.css";

const JoinGame = () => {
    const [token, setToken] = useState("");
    const [validationError, setValidationError] = useState("");
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault(); // Prevent default form submission
        setIsPending(true); // Set pending state

        const gameTokenDTO = { token };

        fetch(`http://localhost:8080/api/v2/games/game/code`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${sessionStorage.getItem("token")}`,
            },
            body: JSON.stringify(gameTokenDTO),
        })
            .then((res) => {
                if (res.ok) {
                    return res.json();
                } else {
                    return res.json().then((err) => {
                        throw new Error(err.message || "Giver and Guesser can't be the same!");
                    });
                }
            })
            .then((data) => {
                console.log(data.gameId); // Log gameId
                navigate("/multi-player/games", { state: { gameId: data.gameId } });
            })
            .catch((error) => {
                setValidationError(error.message || "Error joining the game");
            })
            .finally(() => setIsPending(false)); // Reset pending state
    };

    return (
        <div className="join-game">
            <h2>Join game with code</h2>
            <form onSubmit={handleSubmit}>
                <label>Enter code:</label>
                <input
                    type="text"
                    required
                    value={token}
                    onChange={(e) => setToken(e.target.value)}
                />
                {validationError && <p id="validation-error">{validationError}</p>}
                {!isPending && <button>Submit</button>}
                {isPending && <button disabled>Submitting...</button>}
            </form>
        </div>
    );
};

export default JoinGame;
