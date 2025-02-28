-- FUNCTION: public.fn_policies(text, integer, character varying, integer, integer)

-- DROP FUNCTION IF EXISTS public.fn_policies(text, integer, character varying, integer, integer);

CREATE OR REPLACE FUNCTION public.fn_policies(
	p_option text,
	p_policieid integer,
	p_employee_username character varying,
	p_sku integer,
	p_quantity integer)
    RETURNS TABLE(policieid integer, employee_username character varying, employee_name character varying, sku integer, quantity integer, fecha date, status text, message text) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
DECLARE
	v_fecha DATE := CURRENT_DATE;
BEGIN
    -- Verificar si la tabla 'policies' existe; si no, crearla
    IF NOT EXISTS (
            SELECT 1 
            FROM information_schema.tables 
            WHERE table_schema = 'public' AND table_name = 'policies'
        ) THEN
            EXECUTE '
                CREATE TABLE policies (
                    id SERIAL PRIMARY KEY,
                    employee_username CHARACTER VARYING(8) NOT NULL,
                    sku INT NOT NULL,
                    quantity INT NOT NULL,
                    fecha DATE DEFAULT CURRENT_DATE
                )
            ';
    END IF;

    IF p_option = 'Create' THEN
        BEGIN
            -- Verificar que los parámetros necesarios no sean NULL
            IF p_employee_username IS NULL OR p_sku IS NULL OR p_quantity IS NULL THEN
	                RETURN QUERY SELECT
		            NULL::INTEGER,
		            NULL::CHARACTER VARYING(8),
					NULL::CHARACTER VARYING(30),
		            NULL::INTEGER,
		            NULL::INTEGER,
		            NULL::DATE,
					'FAILURE'::TEXT AS status,
		            'Debe proporcionar username del empleado, sku y cantidad.'::TEXT AS message;
                RETURN;
            END IF;

			-- Verificar si el producto existe en inventory
	        IF NOT EXISTS (
	            SELECT 1 
	            FROM public.inventory AS inv
	            WHERE inv.sku = p_sku
	        ) THEN
	            RETURN QUERY SELECT
	            NULL::INTEGER,
	            NULL::CHARACTER VARYING(8),
				NULL::CHARACTER VARYING(30),
	            NULL::INTEGER,
	            NULL::INTEGER,
	            NULL::DATE,
				'FAILURE'::TEXT AS status,
	            ('El producto no existe en el inventario.')::TEXT AS message;
				RETURN;
	        END IF;

            -- Verificar si hay cantidad suficiente en inventory
	        IF NOT EXISTS (
	            SELECT 1 
	            FROM public.inventory AS inv
	            WHERE inv.sku = p_sku AND inv.quantity >= p_quantity
	        ) THEN
	            RETURN QUERY SELECT
	            NULL::INTEGER,
	            NULL::CHARACTER VARYING(8),
				NULL::CHARACTER VARYING(30),
	            NULL::INTEGER,
	            NULL::INTEGER,
	            NULL::DATE,
				'FAILURE'::TEXT AS status,
	            'No hay suficiente cantidad en el inventario'::TEXT AS message;
				RETURN;
	        END IF;

			-- Verificar si el empleado existe en employee
	        IF NOT EXISTS (
	            SELECT 1 
	            FROM public.employee AS emp
	            WHERE emp.username = p_employee_username
	        ) THEN
	            RETURN QUERY SELECT
	            NULL::INTEGER,
	            NULL::CHARACTER VARYING(8),
				NULL::CHARACTER VARYING(30),
	            NULL::INTEGER,
	            NULL::INTEGER,
	            NULL::DATE,
				'FAILURE'::TEXT AS status,
	            'El empleado ' || p_employee_username || ' no existe.'::TEXT AS message;
				RETURN;
	        END IF;

             -- Crear una nueva poliza
	        INSERT INTO public.policies (employee_username, sku, quantity, fecha)
	        VALUES (p_employee_username, p_sku, p_quantity, v_fecha)
	        RETURNING id INTO policieId;
			
			 -- Actualizar la cantidad en inventory
	        UPDATE public.inventory as inv
	        SET quantity = inv.quantity - p_quantity
	        WHERE inv.sku = p_sku;

			-- Obtener el nombre del empleado
	        SELECT name INTO employee_name
	        FROM public.employee AS emp
	        WHERE emp.username = p_employee_username;

			-- Asignar los demás valores de retorno
	        employee_username := p_employee_username;
	        sku := p_sku;
	        quantity := p_quantity;
	        fecha := v_fecha;
	        status := 'Éxito';
	        message := 'Póliza creada exitosamente.';
	
	        RETURN NEXT;
			
        EXCEPTION
            WHEN others THEN
				RETURN QUERY SELECT
		            NULL::INTEGER,
		            NULL::CHARACTER VARYING(8),
					NULL::CHARACTER VARYING(30),
		            NULL::INTEGER,
		            NULL::INTEGER,
		            NULL::DATE,
					'FAILURE'::TEXT AS status,
		            ('Error al crear la Póliza: ' || SQLERRM)::TEXT AS message;
        END;

		ELSIF p_option = 'Delete' THEN
        -- Bloque para eliminar una póliza
        -- Verificar que se proporcione el ID de la póliza a eliminar
        IF p_policieid IS NULL THEN
            RETURN QUERY SELECT
                NULL::INTEGER,
                NULL::CHARACTER VARYING,
                NULL::CHARACTER VARYING,
                NULL::INTEGER,
                NULL::INTEGER,
                NULL::DATE,
                'FAILURE'::TEXT AS status,
                'Debe proporcionar el ID de la póliza a eliminar.'::TEXT AS message;
            RETURN;
        END IF;

        -- Verificar si la póliza existe
        IF NOT EXISTS (
            SELECT 1
            FROM public.policies AS pol
            WHERE pol.id = p_policieid
        ) THEN
            RETURN QUERY SELECT
                NULL::INTEGER,
                NULL::CHARACTER VARYING,
                NULL::CHARACTER VARYING,
                NULL::INTEGER,
                NULL::INTEGER,
                NULL::DATE,
                'FAILURE'::TEXT AS status,
                'La póliza con ID ' || p_policieid || ' no existe.'::TEXT AS message;
            RETURN;
        END IF;

        -- Eliminar la póliza
        DELETE FROM public.policies AS pol
        WHERE pol.id = p_policieid;
		
        status := 'Éxito';
        message := 'Póliza eliminada exitosamente.';
        policieid := p_policieid;

        RETURN NEXT;
		
		
	ELSIF p_option = 'SelectAll' THEN
	        -- Bloque para consultar todas las pólizas
	        RETURN QUERY
	        SELECT
	            pol.id AS policieid,
	            pol.employee_username,
	            emp.name AS employee_name,
	            pol.sku,
	            pol.quantity,
	            pol.fecha,
	            'Éxito'::TEXT AS status,
	            'Consulta exitosa.'::TEXT AS message
	        FROM public.policies AS pol
	        LEFT JOIN public.employee AS emp ON pol.employee_username = emp.username
	        ORDER BY pol.fecha DESC;

			ELSIF p_option = 'SelectById' THEN
        -- Bloque para consultar una póliza específica por su ID

        -- Verificar que se proporcione el ID de la póliza
        IF p_policieid IS NULL THEN
            RETURN QUERY SELECT
                NULL::INTEGER AS policieid,
                NULL::CHARACTER VARYING AS employee_username,
                NULL::CHARACTER VARYING AS employee_name,
                NULL::INTEGER AS sku,
                NULL::INTEGER AS quantity,
                NULL::DATE AS fecha,
                'FAILURE'::TEXT AS status,
                'Debe proporcionar el ID de la póliza.'::TEXT AS message;
            RETURN;
        END IF;

        -- Verificar si la póliza existe
        IF NOT EXISTS (
            SELECT 1
            FROM public.policies AS pol
            WHERE pol.id = p_policieid
        ) THEN
            RETURN QUERY SELECT
                NULL::INTEGER AS policieid,
                NULL::CHARACTER VARYING AS employee_username,
                NULL::CHARACTER VARYING AS employee_name,
                NULL::INTEGER AS sku,
                NULL::INTEGER AS quantity,
                NULL::DATE AS fecha,
                'FAILURE'::TEXT AS status,
                'La póliza con ID ' || p_policieid || ' no existe.'::TEXT AS message;
            RETURN;
        END IF;

        -- Consultar la póliza específica
        RETURN QUERY
        SELECT
            pol.id AS policieid,
            pol.employee_username,
            emp.name AS employee_name,
            pol.sku,
            pol.quantity,
            pol.fecha,
            'Éxito'::TEXT AS status,
            'Consulta exitosa.'::TEXT AS message
        FROM public.policies AS pol
        LEFT JOIN public.employee AS emp ON pol.employee_username = emp.username
        WHERE pol.id = p_policieid;

		
    ELSE
        RETURN QUERY SELECT
            NULL::INTEGER,
            NULL::CHARACTER VARYING(8),
			NULL::CHARACTER VARYING(30),
            NULL::INTEGER,
            NULL::INTEGER,
            NULL::DATE,
			'FAILURE'::TEXT AS status,
            'Opción no válida.'::TEXT AS message;
    END IF;
END;
$BODY$;

ALTER FUNCTION public.fn_policies(text, integer, character varying, integer, integer)
    OWNER TO postgres;
