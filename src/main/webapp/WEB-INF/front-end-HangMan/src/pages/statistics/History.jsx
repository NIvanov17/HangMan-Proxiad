
import { useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import "./History.css";

const History = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [isPending, setIsPending] = useState(false);
    const { username } = location.state || {};
    const [historyData, setHistoryData] = useState([]);

    const resumeSinglePlayer = (gameId) => {
        navigate('/single-player/games', { state: { gameId } });
    }

    const resumeMultiPlayer = (gameId) => {
        navigate('/multi-player/games', { state: { gameId } });
    }

    useEffect(() => {
        fetch(`http://localhost:8080/api/v1/games/history?username=${username}`)
            .then(res => res.json())
            .then(data => {
                setHistoryData(data)
                console.log(data);
            })
    }, []);


    return (
        <div className="history">
            <h2>History</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Mode</th>
                        <th>Tries Left</th>
                        <th>Category</th>
                        <th>Word State</th>
                        <th>Is Finished</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {historyData.map((game) => (
                        <tr key={game.gameId}>
                            <td>{game.gameId}</td>
                            <td>{game.gameMode}</td>
                            <td>{game.triesLeft}</td>
                            <td>{game.category}</td>
                            <td>{game.finished ? game.word :
                                game.wordProgress}</td>
                            <td>{game.finished ? "Yes" : "No"}</td>
                            <td>
                                {game.gameMode === 'Single Player' ? (
                                    <button
                                        onClick={() => resumeSinglePlayer(game.gameId)}
                                        disabled={game.finished}
                                    >
                                        Resume Game
                                    </button>
                                ) :
                                    (
                                        <button
                                            onClick={() => resumeMultiPlayer(game.gameId)}
                                            disabled={game.finished}
                                        >
                                            Resume Game
                                        </button>
                                    )
                                }
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default History;