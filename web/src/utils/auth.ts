import { writable, derived } from "svelte/store";

const parseJwt = (token: string) => {
    if (!token) return null;
    try {

        const base64Url = token.split(".")[1];
        const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
        const jsonPayload = decodeURIComponent(
            window
                .atob(base64)
                .split("")
                .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
                .join("")
        );

        return JSON.parse(jsonPayload);
    } catch (error) {
        console.error("Error parsing JWT: ", error)
        return null;
    }
};

let initialJWT = localStorage.getItem("JWT") || null;
let initialRole = localStorage.getItem("role") || null;

export let JWT = writable(initialJWT);
export let role = writable(initialRole);
export let username = writable(null);

export let jwt_value: string | null = null;
export let role_value: string | null = null;

JWT.subscribe((value) => {
    jwt_value = value;
    if (value) {
        localStorage.setItem("JWT", value);
        const payload = parseJwt(value);
        if (payload) {
            username.set(payload.sub);
        } else {
            username.set(null);
        }
    } else {
        username.set(null);
        localStorage.removeItem("JWT");
    }
});

role.subscribe((value) => {
    role_value = value;
    if (value) {
        localStorage.setItem("role", value);
    } else {
        localStorage.removeItem("role");
    }
});

export let isAuthenticated = derived(JWT, ($JWT) => !!$JWT);

// isAuthenticated.subscribe((auth) => {
//     if (auth && window.location.hash === "#/login") {
//         window.location.href = "/";
//     }
// });

export const login = async (username: string, password: string, API_HOST: string) => {
    try {
        const response = await fetch(`${API_HOST}auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password }),
        });

        const data = await response.json();

        if (response.ok) {
            JWT.set(data.token);
            role.set(data.role);
            return { success: true, token: data.token };
        } else {
            return { success: false, message: data.message || "Unknown error" };
        }
    } catch (error) {
        return { success: false, message: "Проверьте почту и пароль" };
    }
};