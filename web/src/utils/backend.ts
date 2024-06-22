
import { JWT, jwt_value, username } from "./auth";
import { PUBLIC_API_HOST } from "$env/static/public";
import { goto } from '$app/navigation';


type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE';

interface ApiRequestOptions {
    method: HttpMethod;
    body?: any;
}


export const get = async <T>(
    url: string
): Promise<T> => {
    return apiRequest(url, { method: "GET" })
}

export const post = async <T>(
    url: string,
    body: any
): Promise<T> => {
    return apiRequest(url, { method: "POST", body: body })
}


export const checkAuth = async(): Promise<boolean> => {
    try {
        await apiRequest("/api/v1/auth/check", {method: "GET"}, false)
    } catch (error: any) {
        if (error.message == "401") return false;
    }
    return true;
}

export const apiRequest = async <T>(
    url: string,
    options: ApiRequestOptions,
    redirectIfNoAuth: boolean = true
): Promise<T> => {

    let host = PUBLIC_API_HOST;
    if (host.endsWith("/")) host = host.substring(0, host.length - 1);
    if (url.startsWith("/")) url = url.substring(1);

    try {
        const headers = {
            Authorization: `Bearer ${jwt_value}`,
            'Content-Type': 'application/json',
        };

        const fetchOptions: RequestInit = {
            method: options.method,
            headers,
        };

        if (options.body) {
            fetchOptions.body = JSON.stringify(options.body);
        }

        const response = await fetch(host + "/" + url, fetchOptions);

        if (!response.ok) {
            // if (response.status == 401 && redirectIfNoAuth) {
            //     // goto('/#/login');
            //     // window.location.reload()
            // } else {
                throw Error("" + response.status)
            // }
        }

        const result: T = await response.json();

        return result;
    } catch (error: any) {
        console.error('Error:', error);
        throw error;
    }
};

